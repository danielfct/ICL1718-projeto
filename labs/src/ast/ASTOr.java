package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.BoolValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTOr implements ASTNode {

	final ASTNode left, right;

	public ASTOr(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left + " || " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);

		if (l instanceof BoolValue && r instanceof BoolValue)
			return new BoolValue(((BoolValue)l).getValue() || ((BoolValue)r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Disjunction Operation: And(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);

		if (l == BoolType.singleton && r == BoolType.singleton)
			return BoolType.singleton;
		else
			throw new TypingException("Wrong types on Disjunction Operation: And(" + l + ", " + r + ")");
	}

	@Override
	public void compile(CodeBlock code) { 
		left.compile(code);
		right.compile(code);
		code.emit_or();
	}

}
