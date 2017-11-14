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
import values.TypeMismatchException;

public interface ASTNode {
	
	class Eval {
		
		final IValue value;
		final MemoryManagement mem;
		
		public Eval(IValue value, MemoryManagement mem) {
			this.value = value;
			this.mem = mem;
		}
		
	}
	
	Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException;
	
	IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException;
	
	void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException;
	
	IType getType(); 
	
}