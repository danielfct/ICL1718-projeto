package ast;

<<<<<<< HEAD
import compiler.CodeBlock;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;
=======
import types.IType;
import types.TypingException;
>>>>>>> 011425bfc3abd7bae4a1a4f9a5c66cf3927df1e8

public interface ASTNode {

	IValue eval() throws TypeMismatchException;
	
	IType typecheck() throws TypingException;
	
	void compile(CodeBlock code);
	
<<<<<<< HEAD
=======
	int eval();
	
//	IType typecheck() throws TypingException;
	
//	void compile(CodeBlock code);
>>>>>>> 011425bfc3abd7bae4a1a4f9a5c66cf3927df1e8
}
