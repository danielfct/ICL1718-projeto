package ast;

import java.util.Objects;

import compiler.ICodeBuilder;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTNot implements ASTNode {

	final ASTNode value;
	private IType type;

	public ASTNot(ASTNode value) {
		this.value = value;
		this.type = null;
	}

	@Override
	public String toString() {
		return "!" + value;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IValue v = value.eval(env);

		if (v instanceof BoolValue)
			return new BoolValue(!((BoolValue) v).getValue());
		else
			throw new TypeMismatchException("Wrong type on Not Operation: Not(" + v + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType v = value.typecheck(env);

		if (v == BoolType.singleton)
			type = BoolType.singleton;
		else
			throw new TypingException("Wrong type on Not Operation: Not(" + v + ")");

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// Since false is represented by an integer of value 0 and
		// true is represented by an integer of value 1, a not operation can be
		// achieved with a xor operation. !B = B^1

		value.compile(code, env);
		code.emit_bool(true);
		code.emit_xor();
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTNot))
			return false;
		ASTNot other = (ASTNot) obj;
		return Objects.equals(value, other.value);
	}

}
