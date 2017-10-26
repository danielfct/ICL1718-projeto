package ast;

import compiler.CodeBlock;
import types.IType;
import types.IntType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTMul implements ASTNode {

	final ASTNode left, right;

	public ASTMul(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left + " * " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);
		
		if (l instanceof IntValue && r instanceof IntValue) {
			return new IntValue(((IntValue)l).getValue() * ((IntValue)r).getValue());
		}
		else {
			throw new TypeMismatchException();
		}
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);
		
		if (l == r)
			return IntType.singleton;
		else
			throw new TypingException();
	}

	@Override
	public void compile(CodeBlock code) {
		left.compile(code);
		right.compile(code);
		code.emit_mul();
	}


}
