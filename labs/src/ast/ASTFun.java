package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import compiler.Closure;
import compiler.ICodeBuilder;
import compiler.StackFrame;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.FunType;
import types.IType;
import types.TypingException;
import values.ClosureValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTFun implements ASTNode {

	public static class Parameter {
		public final String id;
		public final IType type;

		public Parameter(String id, IType type) {
			this.id = id;
			this.type = type;
		}

		@Override
		public String toString() {
			return id + ":" + type;
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
			if (!(obj instanceof Parameter))
				return false;
			Parameter other = (Parameter) obj;
			return Objects.equals(id, other.id) && Objects.equals(type, other.type);
		}

	}

	final List<Parameter> parameters;
	final ASTNode body;
	private IType type;

	public ASTFun(List<Parameter> parameters, ASTNode body) {
		this.parameters = parameters;
		this.body = body;
		this.type = null;
	}

	@Override
	public String toString() {
		return "fun " + String.join(", ", parameters.stream().map(Parameter::toString).collect(Collectors.toList())) + " -> " + body + " end";
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		return new ClosureValue(parameters, body, env);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IType> newEnv = env.beginScope();
		List<IType> paramsType = new ArrayList<IType>(parameters.size());
		for (Parameter param : parameters) {
			newEnv.assoc(param.id, param.type);
			paramsType.add(param.type);
		}
		IType retType = body.typecheck(newEnv);
		type = new FunType(paramsType, retType);
		env = newEnv.endScope();

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		Closure closure = code.newClosure((FunType)type);
		code.emit_new(closure.name);
		code.emit_dup();
		code.emit_invokespecial(closure.name, "<init>", "()V");
		// Set closure's frame  
		StackFrame currentFrame = code.getCurrentFrame();
		if (currentFrame != null) {
			code.emit_comment("initialize closure's SL");
			code.emit_dup();
			code.emit_aload(code.getSPPosition());
			code.emit_checkcast(currentFrame.name);
			code.emit_putfield(closure.name, "SL", closure.env.toJasmin());
		}
		// Start a new frame because a function has its own scope
		StackFrame frame = code.newFrame();
		code.setCurrentFrame(frame);
		closure.emit_comment("start new frame");
		closure.emit_new(frame.name);
		closure.emit_dup();
		closure.emit_invokespecial(frame.name, "<init>", "()V");
		if (frame.ancestor != null) {
			closure.emit_comment("initialize frame's SL");
			closure.emit_dup();
			closure.emit_aload(0); // this
			closure.emit_getfield(closure.name, "SL", closure.env.toJasmin());
			closure.emit_putfield(frame.name, "SL", frame.ancestor.toJasmin());
		}
		// Initialize parameters
		ICompilationEnvironment newEnv = env.beginScope();
		int position = 1; // position 0 reserved for 'this'
		for (Parameter param : parameters) {
			closure.emit_comment("initialize param " + param.id);
			closure.emit_dup();
			if (code.toJasmin(param.type).matches("L(.*);"))
				closure.emit_aload(position++); // param.id argument is not primitive
			else
				closure.emit_iload(position++); // param.id argument is primitive
			int location = frame.nextLocation();
			newEnv.assoc(param.id, location);
			String type = code.toJasmin(param.type);
			frame.setLocation(location, type);
			closure.emit_putfield(frame.name, "loc_" + String.format("%02d", location), type);
		}
		closure.emit_astore(closure.getSPPosition());
		closure.emit_comment("->");
		body.compile(closure, newEnv);

		closure.emit_comment("end");
		if (frame.ancestor != null) {
			closure.emit_aload(closure.getSPPosition());
			closure.emit_checkcast(frame.name);
			closure.emit_getfield(frame.name, "SL", frame.ancestor.toJasmin());
		} else {
			closure.emit_null();
		}
		closure.emit_astore(closure.getSPPosition());
		closure.setCurrentFrame(frame.ancestor);
		newEnv.endScope();
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameters, body);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTFun))
			return false;
		ASTFun other = (ASTFun) obj;
		return Objects.equals(parameters, other.parameters) && Objects.equals(body, other.body);
	}

}
