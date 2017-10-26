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
	public IValue eval(IEnvironment<IValue> env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		return new BoolValue(val);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		return BoolType.singleton;
	}

	@Override
	public void compile(CodeBlock code) {
		code.emit_bool(val);
	}
	
}
