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
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IValue v = value.eval(env);
		
		if (v instanceof BoolValue)
			return new BoolValue(!((BoolValue)v).getValue());
		else
			throw new TypeMismatchException("Wrong type on Not Operation: Not(" + v + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IType v = value.typecheck(env);
		
		if (v == BoolType.singleton)
			return BoolType.singleton;
		else
			throw new TypingException("Wrong type on Not Operation: Not(" + v + ")");
	}

	@Override
	public void compile(CodeBlock code) {
		// Since false is represented by an integer of value 0 and
		// true is represented by an integer of value 1, a not operation can be
		// achieved with a xor operation. !B = B^1
		
		value.compile(code);
		code.emit_bool(true);
		code.emit_xor();
	}

}
