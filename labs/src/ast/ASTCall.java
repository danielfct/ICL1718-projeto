package ast;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

import ast.ASTFunction.Parameter;
import compiler.CodeBlock;
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
	final Collection<ASTNode> arguments;
	private IType type;

	public ASTCall(ASTNode function, Collection<ASTNode> arguments) {
		this.function = function;
		this.arguments = arguments;
		this.type = null;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue f = function.eval(env);

		if (f instanceof ClosureValue) {
			ClosureValue closure = (ClosureValue) f;
			IEnvironment<IValue> newEnv = closure.env.beginScope();
			Iterator<String> params = closure.params.stream().map(Parameter::getId).collect(Collectors.toList()).iterator();
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
			Iterator<IType> params = fun.parameters.iterator();
			Iterator<ASTNode> args = arguments.iterator();
			while (params.hasNext() && args.hasNext()) {
				if (!params.next().equals(args.next().typecheck(env)))
					throw new TypingException("Function expecting arguments of type " + params + " and got " + args + ".");
			}
			type = fun.ret;
		} else {
			throw new TypingException(f + " is not a function.");
		}

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env)
			throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// TODO Auto-generated method stub

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
