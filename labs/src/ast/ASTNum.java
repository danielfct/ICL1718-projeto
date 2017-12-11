package ast;

import java.util.Objects;

import compiler.ICodeBuilder;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.IntType;
import types.TypingException;
import values.IValue;
import values.IntValue;

public class ASTNum extends ASTNode {

	final int value;
	private IType type;

	public ASTNum(int value) {
		this.value = value;
		this.type = null;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		return new IntValue(value);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		type = IntType.singleton;
		
		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_push(value);
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
		if (!(obj instanceof ASTNum))
			return false;
		ASTNum other = (ASTNum) obj;
		return Objects.equals(value, other.value);
	}

}
