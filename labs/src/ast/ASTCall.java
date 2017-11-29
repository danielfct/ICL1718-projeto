package ast;

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
	final ASTNode args;
	private IType type;
	
	public ASTCall(ASTNode function, ASTNode args) {
		this.function = function;
		this.args = args;
		this.type = null;
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env)
			throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue fun = function.eval(env);
		if (fun instanceof ClosureValue) {
			ClosureValue closure = (ClosureValue)fun;
			IEnvironment<IValue> newEnv = env.beginScope();
			newEnv.assoc(closure.parameter, args.eval(env));
			IValue value = closure.body.eval(newEnv);
			newEnv.endScope();
			return value;
		}
		else 
			throw new TypeMismatchException("Wrong type on Call Operation: call(" + fun + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env)
			throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType fun = function.typecheck(env);
		IType tp = args.typecheck(env);
		if (fun instanceof FunType && tp.equals(((FunType)fun).parameterType))
			return type = ((FunType)fun).returnType;
		else
			throw new TypingException("Wrong types on Call Operation: call(" + fun + ", " + tp + ")");
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

}
