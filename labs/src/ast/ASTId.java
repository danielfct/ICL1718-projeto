package ast;

import compiler.CodeBlock;
import types.IType;
import types.TypingException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.IValue;

public class ASTId implements ASTNode {
	
	String id;
	
	public ASTId(String id) {
		this.id = id;
	}

	@Override 
	public IValue eval(IEnvironment<IValue> env) throws UndeclaredIdentifierException {	
		return env.find(id);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException {
		return env.find(id);
	}

	@Override
	public void compile(CodeBlock code) {
		
		// Get the stack pointer
		/*
		aload SP
		checkcast frame_id
		*/
		// ask code for the current frame

		// ask env for the address (jumps, loc_X)
		// class Address { int jumps; int loc; ... }
		// Address addr = env.lookup(id) <<< Lookup in a class that extends Environment 
		// for each jump:
			/*
			getfield frame_n/SL Lframe_n1;
			getfield frame_n2/SL Lframe_n2;
			getfield frame_n2/SL Lframe_n3;
			…
			*/
		/*
		getfield frame_1/loc_X I ; <<<< I is the type of the identifier

		 */
		// use getType to know the field type
		
		
		
		
	}
}