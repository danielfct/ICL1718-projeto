package ast;

import compiler.CodeBlock;
import types.IType;
import types.IntType;
import types.TypingException;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTSym implements ASTNode {

	final ASTNode value;

	public ASTSym(ASTNode value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "-" + value;
	}

	@Override
	public IValue eval() throws TypeMismatchException {
		IValue v = value.eval();
		
		if (v instanceof IntValue) {
			return new IntValue(-((IntValue)v).getValue());
		}
		else {
			throw new TypeMismatchException();
		}
	}

	@Override
	public IType typecheck() throws TypingException {
		IType v = value.typecheck();
		
		if (v == IntType.singleton)
			return IntType.singleton;
		else 
			throw new TypingException();
	}

	@Override
	public void compile(CodeBlock code) {
		// TODO Auto-generated method stub
		
	}

}
