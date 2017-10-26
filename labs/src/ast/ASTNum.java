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
	public IValue eval(IEnvironment<IValue> env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		return new IntValue(val);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		return IntType.singleton;
	}

	@Override
	public void compile(CodeBlock code) {
		code.emit_push(val);
	}
}
