package ast;

import compiler.CodeBlock;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public interface ASTNode {

	IValue eval() throws TypeMismatchException;
	
	IType typecheck() throws TypingException;
	
	void compile(CodeBlock code);
	
}
