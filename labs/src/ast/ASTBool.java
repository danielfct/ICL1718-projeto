package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;

public class ASTBool implements ASTNode {

	boolean val;

	public ASTBool(boolean b) {
		val = b;
	}

	@Override
	public String toString() {
		return Boolean.toString(val);
	}

	@Override
	public IValue eval() {
		return new BoolValue(val);
	}

	@Override
	public IType typecheck() throws TypingException {
		return BoolType.singleton;
	}

	@Override
	public void compile(CodeBlock code) {
		// TODO Auto-generated method stub
		
	}
	
}
