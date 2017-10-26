package ast;

import compiler.CodeBlock;
import types.IType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.IValue;
import values.TypeMismatchException;

public interface ASTNode {
	
	IValue eval(IEnvironment<IValue> env) 
			throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException;
	
	IType typecheck(IEnvironment<IType> env) 
			throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException;
	
	void compile(CodeBlock code);
}

