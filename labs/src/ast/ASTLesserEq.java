package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTLesserEq implements ASTNode {

	final ASTNode left, right;

	public ASTLesserEq(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left + " <= " + right;
	}

	@Override
	public IValue eval() throws TypeMismatchException {
		IValue l = left.eval();
		IValue r = right.eval();

		if (l instanceof IntValue && r instanceof IntValue) {
			return new BoolValue(((IntValue)l).getValue() <= ((IntValue)r).getValue());
		}
		else {
			throw new TypeMismatchException();
		}
	}

	@Override
	public IType typecheck() throws TypingException {
		IType l = left.typecheck();
		IType r = right.typecheck();
		
		if (l == r)
			return BoolType.singleton;
		else
			throw new TypingException();
	}

	@Override
	public void compile(CodeBlock code) {
		// TODO Auto-generated method stub
		
	}

}
