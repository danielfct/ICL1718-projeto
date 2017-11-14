package ast;

import compiler.CodeBlock;
import compiler.StackFrame;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.IType;
import types.TypingException;
import values.IValue;

public class ASTId implements ASTNode {

	public static class Address { 
		final int jumps; 
		final int location; 

		public Address(int jumps, int location) {
			this.jumps = jumps;
			this.location = location;
		}
	}

	final String id;
	private IType type;

	public ASTId(String id) {
		this.id = id;
		this.type = null;
	}

	@Override 
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws UndeclaredIdentifierException {	
		return env.find(id);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		type = env.find(id);
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// Get the stack pointer
		code.emit_SP();
		
		// Ask code for the current frame
		StackFrame currentFrame = code.getCurrentFrame();

		code.emit_comment("Get " + id);

		// Ask Environment for the address (num jumps, location)
		Address addr = env.lookup(id);

		// Get Static Links
		for (int i = 0; i < addr.jumps; i++) {
			code.emit_getfield("Frame_" + currentFrame.id, "SL", "LFrame_" + currentFrame.ancestor.id + ";");
			currentFrame = currentFrame.ancestor;
		}
		// Get Id
		code.emit_getfield("Frame_" + currentFrame.id, "loc_" + String.format("%02d", addr.location), getType().toString());
	}

	@Override
	public IType getType() {
		return type;
	}
	
}