package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.IType;
import types.IntType;
import types.TypingException;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTAdd implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTAdd(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " + " + right;
	}

	@Override
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, 
	DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env, mem);
		IValue r = right.eval(env, mem);

		if (l instanceof IntValue && r instanceof IntValue)
			return new IntValue(((IntValue)l).getValue() + ((IntValue)r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Addition Operation: Add(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env); 
		IType r = right.typecheck(env);
		
		if (l == IntType.singleton && r == IntType.singleton)
			type = IntType.singleton;
		else
			throw new TypingException("Wrong types on Addition Operation: Add(" + l + ", " + r + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		left.compile(code, env);
		right.compile(code, env);
		code.emit_add();
	} 

	@Override
	public IType getType() {
		return type;
	}

}
