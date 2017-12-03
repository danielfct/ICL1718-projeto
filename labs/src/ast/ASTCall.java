package ast;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import compiler.ICodeBuilder;
import compiler.TypeSignature;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.FunType;
import types.IType;
import types.TypingException;
import values.ClosureValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTCall implements ASTNode {

	final ASTNode function;
	final List<ASTNode> arguments;
	private IType type;

	public ASTCall(ASTNode function, List<ASTNode> arguments) {
		this.function = function;
		this.arguments = arguments;
		this.type = null;
	}

	@Override
	public String toString() {
		return function + "(" + String.join(",", arguments.stream().map(ASTNode::toString).collect(Collectors.toList())) + ")";
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue f = function.eval(env);

		if (f instanceof ClosureValue) {
			ClosureValue closure = (ClosureValue) f;
			IEnvironment<IValue> newEnv = closure.env.beginScope();
			Iterator<String> params = closure.params.stream().map(p -> p.id).collect(Collectors.toList()).iterator();
			Iterator<ASTNode> args = arguments.iterator();
			while (params.hasNext() && args.hasNext()) {
				newEnv.assoc(params.next(), args.next().eval(env)); // TODO
																	// verificar
																	// tipo dos
																	// argumentos
			}
			IValue value = closure.body.eval(newEnv);
			newEnv.endScope();
			return value;
		} else {
			throw new TypeMismatchException(f + " is not a function.");
		}
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType f = function.typecheck(env);

		if (f instanceof FunType) {
			FunType fun = (FunType) f;
			Iterator<IType> params = fun.paramsType.iterator();
			Iterator<ASTNode> args = arguments.iterator();
			while (params.hasNext() && args.hasNext()) {
				if (!params.next().equals(args.next().typecheck(env)))
					throw new TypingException("Function expecting arguments of type " + fun.paramsType + " and got " + arguments + ".");
			}
			type = fun.retType;
		} else {
			throw new TypingException(f + " is not a function.");
		}

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {	
		function.compile(code, env);
		FunType type = (FunType)function.getType();
		TypeSignature signature = code.getSignature(type);
		code.emit_checkcast(signature.name);
		for (ASTNode argument : arguments)
			argument.compile(code, env);
		code.emit_invokeinterface(signature.name, "call", signature.toJasmin(), arguments.size() + 1);
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(function, arguments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTCall))
			return false;
		ASTCall other = (ASTCall) obj;
		return Objects.equals(function, other.function) && Objects.equals(arguments, other.arguments);
	}

}
