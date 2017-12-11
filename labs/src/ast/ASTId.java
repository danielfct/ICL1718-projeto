package ast;

import java.util.Objects;

import compiler.ICodeBuilder;
import compiler.StackFrame;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class ASTId extends ASTNode {

	public static class Address {
		final int jumps;
		final int location;

		public Address(int jumps, int location) {
			this.jumps = jumps;
			this.location = location;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(jumps, location);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Address))
				return false;
			Address other = (Address) obj;
			return Objects.equals(jumps, other.jumps) && Objects.equals(location, other.location);
		}
		
		@Override
		public String toString() {
			return "jumps: " + jumps + ", location: " + location;
		}
		
	}

	final String id;
	private IType type;

	public ASTId(String id) {
		this.id = id;
		this.type = null;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		return force(env.find(id));
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		type = env.find(id);
		
		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_comment("Get " + id);
		// Ask code for the current frame
		StackFrame currentFrame = code.getCurrentFrame();
		// Get the stack pointer
		if (currentFrame != null) {
			code.emit_aload(code.getCurrentSP());
			code.emit_checkcast(currentFrame.name);
		}
		// Ask Environment for the address (num jumps, location)
		Address addr = env.lookup(id);
		// Get Static Links
		for (int i = 0; i < addr.jumps; i++) {
			code.emit_getfield(currentFrame.name, "SL", currentFrame.ancestor.toJasmin());
			currentFrame = currentFrame.ancestor;
		}
		// Get Id
		code.emit_getfield(currentFrame.name, "loc_" + addr.location, code.toJasmin(type));
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTId))
			return false;
		ASTId other = (ASTId) obj;
		return Objects.equals(id, other.id) && Objects.equals(type, other.getType());
	}
	
	@Override
	public String toString() {
		return id;
	}

}
