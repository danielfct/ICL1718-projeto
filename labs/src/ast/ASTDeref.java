package ast;

import java.util.Objects;

import compiler.CodeBlock;
import compiler.Reference;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.Memory;
import types.IType;
import types.RefType;
import types.TypingException;
import values.IValue;
import values.IRefValue;
import values.TypeMismatchException;

public class ASTDeref implements ASTNode {

	final ASTNode expression;
	private IType type;

	public ASTDeref(ASTNode n) {
		this.expression = n;
		this.type = null;
	}

	@Override
	public String toString() {
		return "*" + expression;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue v = expression.eval(env);

		if (v instanceof IRefValue)
			return Memory.singleton.get((IRefValue) v);
		else
			throw new TypeMismatchException("Wrong type on Deref Operation: Deref(" + v + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType t = expression.typecheck(env);

		if (t instanceof RefType)
			type = ((RefType) t).type;
		else
			throw new TypingException("Wrong type on Deref Operation: Deref(" + t + ")");

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		expression.compile(code, env);
		IType type = ((RefType) expression.getType()).type;
		Reference ref = code.getReference(type);
		code.emit_getfield(ref.name, "value", code.toJasmin(ref.type));
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(expression);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTDeref))
			return false;
		ASTDeref other = (ASTDeref) obj;
		return Objects.equals(expression, other.expression);
	}

}
