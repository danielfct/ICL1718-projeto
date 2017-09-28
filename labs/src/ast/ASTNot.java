package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTNot implements ASTNode {

	final ASTNode value;

	public ASTNot(ASTNode value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "!" + value;
	}

	@Override
	public IValue eval() throws TypeMismatchException {
		IValue v = value.eval();
		
		if (v instanceof BoolValue) {
			return new BoolValue(!((BoolValue)v).getValue());
		}
		else {
			throw new TypeMismatchException();
		}
	}

	@Override
	public IType typecheck() throws TypingException {
		IType v = value.typecheck();

		if (v == BoolType.singleton)
			return BoolType.singleton;
		else
			throw new TypingException();
	}

	@Override
	public void compile(CodeBlock code) {
		// TODO Auto-generated method stub

	}

}
