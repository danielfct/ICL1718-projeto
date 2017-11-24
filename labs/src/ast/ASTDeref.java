package ast;

import compiler.CodeBlock;
import compiler.Reference;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.Memory;
import types.IType;
import types.RefType;
import types.TypingException;
import values.IValue;
import values.IRefValue;
import values.TypeMismatchException;

public class ASTDeref implements ASTNode {

	final ASTNode node;
	private IType type;
	
	public ASTDeref(ASTNode n) {
		this.node = n;
		this.type = null;
	}
	
	@Override
	public String toString() {
		return "*" + node;
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue v = node.eval(env);
		
		if (v instanceof IRefValue)
			return Memory.singleton.get((IRefValue)v);
		else
			throw new TypeMismatchException("Wrong type on Deref Operation: Deref(" + v + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType t = node.typecheck(env);
		
		if (t instanceof RefType)
			type = ((RefType)t).getType();
		else
			throw new TypingException("Wrong type on Deref Operation: Deref(" + t + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		node.compile(code, env);
		IType type = ((RefType)node.getType()).getType();
		Reference ref = code.getReference(type);
		code.emit_getfield(ref.name, "value", ref.getType());	
	}

	@Override
	public IType getType() {
		return type;
	}

}
