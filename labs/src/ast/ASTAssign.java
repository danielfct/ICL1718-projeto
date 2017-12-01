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

public class ASTAssign implements ASTNode {

	final ASTNode reference;
	final ASTNode expression;
	private IType type;

	public ASTAssign(ASTNode reference, ASTNode expression) {
		this.reference = reference;
		this.expression = expression;
		this.type = null;
	}

	@Override
	public String toString() {
		return reference + " := " + expression;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue ref = reference.eval(env);
		IValue v = expression.eval(env);

		if (ref instanceof IRefValue)
			return Memory.singleton.set((IRefValue) ref, v);
		else
			throw new TypeMismatchException("Wrong types on Assign Operation: :=(" + ref + ", " + v + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType ref = reference.typecheck(env);
		IType t = expression.typecheck(env);

		if (ref.equals(new RefType(t)))
			type = t;
		else
			throw new TypingException("Wrong types on Assign Operation: :=(" + ref + ", " + t + ")");

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		reference.compile(code, env);
		code.emit_dup();
		expression.compile(code, env);
		IType type = expression.getType();
		Reference ref = code.getReference(type);
		code.emit_putfield(ref.name, "value", code.toJasmin(ref.type));
		code.emit_getfield(ref.name, "value", code.toJasmin(ref.type));
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reference, expression);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTAssign))
			return false;
		ASTAssign other = (ASTAssign) obj;
		return Objects.equals(reference, other.reference) && Objects.equals(expression, other.expression);
	}

}
