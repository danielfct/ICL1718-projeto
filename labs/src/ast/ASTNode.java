package ast;

import types.IType;
import types.TypingException;
import util.IEnvironment;
import values.IValue;

public interface ASTNode {
	
	int eval(IEnvironment<IValue> env);
	
//	IType typecheck(IEnvironment<IType> env) throws TypingException;
	
//	void compile(CodeBlock code);
}

