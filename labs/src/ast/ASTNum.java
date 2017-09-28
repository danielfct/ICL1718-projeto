package ast;

import compiler.CodeBlock;
import types.IType;
import types.IntType;
import types.TypingException;
import values.IValue;
import values.IntValue;

public class ASTNum implements ASTNode {

	int val;

	public ASTNum(int n) {
		val = n;
	}

	@Override
	public String toString() {
		return Integer.toString(val);
	}

	@Override
	public IValue eval() {
		return new IntValue(val);
	}

	@Override
	public IType typecheck() throws TypingException {
		return IntType.singleton;
	}

	@Override
	public void compile(CodeBlock code) {
		// TODO Auto-generated method stub
		
	}
}
