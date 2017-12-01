package ast;

import java.util.Objects;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.IntType;
import types.TypingException;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTMul implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTMul(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " * " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);

		if (l instanceof IntValue && r instanceof IntValue)
			return new IntValue(((IntValue) l).getValue() * ((IntValue) r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Multiplication Operation: Mul(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);

		if (l == IntType.singleton && r == IntType.singleton)
			type = IntType.singleton;
		else
			throw new TypingException("Wrong types on Multiplication Operation: Mul(" + l + ", " + r + ")");

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		left.compile(code, env);
		right.compile(code, env);
		code.emit_mul();
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTMul))
			return false;
		ASTMul other = (ASTMul) obj;
		return Objects.equals(left, other.left) && Objects.equals(right, other.right);
	}

}
