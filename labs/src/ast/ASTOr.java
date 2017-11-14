package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTOr implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTOr(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " || " + right;
	}

	@Override
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env, mem);
		IValue r = right.eval(env, mem);

		if (l instanceof BoolValue && r instanceof BoolValue)
			return new BoolValue(((BoolValue)l).getValue() || ((BoolValue)r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Disjunction Operation: And(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);

		if (l == BoolType.singleton && r == BoolType.singleton)
			type = BoolType.singleton;
		else
			throw new TypingException("Wrong types on Disjunction Operation: And(" + l + ", " + r + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException { 
		left.compile(code, env);
		right.compile(code, env);
		code.emit_or();
	}

	@Override
	public IType getType() {
		return type;
	}

}
