package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.Memory;
import types.IType;
import types.RefType;
import types.TypingException;
import values.IValue;
import values.RefValue;
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
		
		if (ref instanceof RefValue)
			return Memory.singleton.set((RefValue)ref, v);
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
		// TODO Auto-generated method stub
	}

	@Override
	public IType getType() {
		return type;
	}

}
