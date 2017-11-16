package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.IntType;
import types.TypingException;
import values.IValue;
import values.IntValue;

public class ASTNum implements ASTNode {

	final int value;

	public ASTNum(int v) {
		value = v;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		return new IntValue(value);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		return IntType.singleton;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_push(value);
	}

	@Override
	public IType getType() {
		return IntType.singleton; 
	}
}
