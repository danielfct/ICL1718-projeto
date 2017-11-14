package ast;

import compiler.CodeBlock;
import types.IType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.ICompilationEnvironment;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.IValue;
import values.TypeMismatchException;

public interface ASTNode {
	
	IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException;
	
	IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException;
	
	void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException;
	
	IType getType(); 
	
}