package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class ASTSeq implements ASTNode {

	final ASTNode left;
	final ASTNode right;
	private IType type;
	
	public ASTSeq(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}
	
	@Override
	public String toString() {
		return left + " ; " + right;
	}
	
	@Override
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(seq(E1, E2), env, m0) = [		(v1, m1) = eval(E1, env, m0);
//											(v2, m2) = eval(E2, env, m1);
//											return (v2, m2) 
//									  ]
		return null;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		// TODO Auto-generated method stub
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
