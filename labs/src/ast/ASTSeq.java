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
		IValue value = right.eval(env);
		
		return value;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		left.typecheck(env);
		type = right.typecheck(env);

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_comment("Starting " + this);
		left.compile(code, env);
		code.emit_pop();
		right.compile(code, env);
		code.emit_comment("Ending " + this);
	}

	@Override
	public IType getType() {
		return type;
	}

}
