package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.IType;
import types.TypingException;
import values.IValue;
import values.RefValue;
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
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(deref(E), env, m0) = [ 	(ref, m1) = eval(E, env, m0);
//										(m1.get(ref), m1) 
//								   ]
		Eval evaluation = node.eval(env, mem);
		IValue ref = evaluation.value;
		MemoryManagement newMem = evaluation.mem;
		IValue value = newMem.get((RefValue)ref);
		
		return new Eval(value, newMem);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(deref(E), env) = [ 	ref{t} = typecheck(E, env ); 
//										t
//									]
		return null;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// TODO Auto-generated method stub
	}

	@Override
	public IType getType() {
		return type;
	}

}
