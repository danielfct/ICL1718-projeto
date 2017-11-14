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

public class ASTVar implements ASTNode {

	final ASTNode node;
	private IType type;
	
	public ASTVar(ASTNode n) {
		this.node = n;
		this.type = null;
	}
	
	
	@Override
	public String toString() {
		return "var(" + node + ")";
	}
	
	@Override
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(var(E), env, m0) = [ 	(v1 , m1) = eval(E, env, m0);
//										(ref, m2) = m1.new(v1);
//										(ref, m2) 
//								 ]
		Eval evaluation = node.eval(env, mem);
		IValue value = evaluation.value;
		MemoryManagement newMem = evaluation.mem;
		RefValue ref = newMem.var(value);
		
		return new Eval(ref, newMem);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(var(E), env ) = [ 	t = typecheck( E, env ); 
//										ref{t}
//								   ]
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
