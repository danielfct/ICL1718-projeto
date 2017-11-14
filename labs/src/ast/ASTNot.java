package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTNot implements ASTNode {

	final ASTNode value;
	private IType type;

	public ASTNot(ASTNode value) {
		this.value = value;
		this.type = null;
	}

	@Override
	public String toString() {
		return "!" + value;
	}

	@Override
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IValue v = value.eval(env, mem);
		
		if (v instanceof BoolValue)
			return new BoolValue(!((BoolValue)v).getValue());
		else
			throw new TypeMismatchException("Wrong type on Not Operation: Not(" + v + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType v = value.typecheck(env);
		
		if (v == BoolType.singleton)
			type = BoolType.singleton;
		else
			throw new TypingException("Wrong type on Not Operation: Not(" + v + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// Since false is represented by an integer of value 0 and
		// true is represented by an integer of value 1, a not operation can be
		// achieved with a xor operation. !B = B^1
		
		value.compile(code, env);
		code.emit_bool(true);
		code.emit_xor();
	}

	@Override
	public IType getType() {
		return type;
	}

}
