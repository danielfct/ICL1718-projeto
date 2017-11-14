package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class ASTDeref implements ASTNode {

	final ASTNode value;
	private IType type;
	
	public ASTDeref(ASTNode value) {
		this.value = value;
		this.type = null;
	}
	
	@Override
	public String toString() {
		return "*" + value;
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(deref(E), env, m0) = [ 	(ref, m1) = eval(E, env, m0);
//										(m1.get(ref), m1) 
//								   ]
		return null;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(deref(E), env) = [ 	ref{t} = typecheck(E, env ); 
//										t
//									]
		return null;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// TODO Auto-generated method stub
	}

	@Override
	public IType getType() {
		return type;
	}

}
