package ast;

import compiler.CodeBlock;
import types.IType;
import types.IntType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.ICompilationEnvironment;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTDiv implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTDiv(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " / " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);
		
		if (l instanceof IntValue && r instanceof IntValue)
			return new IntValue(((IntValue)l).getValue() / ((IntValue)r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Division Operation: Div(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);
		
		if (l == IntType.singleton && r == IntType.singleton)
			type = IntType.singleton;
		else
			throw new TypingException("Wrong types on Division Operation: Div(" + l + ", " + r + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		left.compile(code, env);
		right.compile(code, env);
		code.emit_div();
	}

	@Override
	public IType getType() {
		return type;
	}

}
