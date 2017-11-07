package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.ICompilationEnvironment;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.BoolValue;
import values.IValue;

public class ASTBool implements ASTNode {

	final boolean val;

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
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		return BoolType.singleton;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_bool(val);
	}

	@Override
	public IType getType() {
		return BoolType.singleton;
	}
	
}
