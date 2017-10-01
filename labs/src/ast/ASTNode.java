package ast;

import types.IType;
import types.TypingException;

public interface ASTNode {
	
	int eval();
	
//	IType typecheck() throws TypingException;
	
//	void compile(CodeBlock code);
}

