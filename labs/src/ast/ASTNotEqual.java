package ast;

import java.util.Objects;

import compiler.ICodeBuilder;
import compiler.IdFactory;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.BoolType;
import types.IType;
import types.IntType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTNotEqual implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTNotEqual(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " != " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);

		if (l instanceof IntValue && r instanceof IntValue)
			return new BoolValue(((IntValue) l).getValue() != ((IntValue) r).getValue());
		else if (l instanceof BoolValue && r instanceof BoolValue)
			return new BoolValue(((BoolValue) l).getValue() != ((BoolValue) r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Not Equal Operation: Neq(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);

		if (l == IntType.singleton && r == IntType.singleton || l == BoolType.singleton && r == BoolType.singleton)
			type = BoolType.singleton;
		else
			throw new TypingException("Wrong types on Not Equal Operation: Neq(" + l + ", " + r + ")");

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		String labelNotEqual = "label" + IdFactory.singleton.label();
		String labelExit = "label" + IdFactory.singleton.label();

		left.compile(code, env);
		right.compile(code, env);
		code.emit_icmpne(labelNotEqual);
		code.emit_bool(false);
		code.emit_jump(labelExit);
		code.emit_anchor(labelNotEqual);
		code.emit_bool(true);
		code.emit_anchor(labelExit);
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
		if (!(obj instanceof ASTNotEqual))
			return false;
		ASTNotEqual other = (ASTNotEqual) obj;
		return Objects.equals(left, other.left) && Objects.equals(right, other.right);
	}

}
