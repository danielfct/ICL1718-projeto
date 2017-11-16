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
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		left.eval(env);
		IValue v = right.eval(env);
		
		return v;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		left.typecheck(env);
		type = right.typecheck(env);
		
		// TODO verificar se está correto
		
		return type;
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
