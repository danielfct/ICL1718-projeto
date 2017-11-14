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

public class ASTAssign implements ASTNode {

	final ASTNode left;
	final ASTNode right;
	private IType type;
	
	public ASTAssign(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}
	
	@Override
	public String toString() {
		return left + " ; " + right;
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(assign(E1, E2), env, m0) = [	(v1, m1) = eval(E1, env, m0);
//											(v2, m2) = eval(E2, env, m1);
//											m3 = m2.set(v1, v2);
//											(v2, m3)
//										 ]
		return null;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(assign(E1, E2) , env ) =	[ 	t1 = typecheck(E1, env);
//												t2 = typecheck(E2, env);
//												if (t1 == ref{t2})
//													then t2;
//												else none; 
//											]
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
