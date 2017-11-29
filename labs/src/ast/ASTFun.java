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

public class ASTFun implements ASTNode {

	final String parameter;
	final IType parameterType;
	final ASTNode body;
	private IType type;
	
	public ASTFun(String parameter, IType parameterType, ASTNode body) {
		this.parameter = parameter;
		this.parameterType = parameterType;
		this.body = body;
		this.type = null;
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env)
			throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		return new ClosureValue(parameter, body, env);
	}
	
	
	@Override
	public IType typecheck(IEnvironment<IType> env)
			throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IType> newEnv = env.beginScope();
		newEnv.assoc(parameter, parameterType);
		IType tr = body.typecheck(newEnv);
		return type = new FunType(parameterType, tr);
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
