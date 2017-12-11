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

public class ASTFun extends ASTNode {

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
	final IASTNode body;
	private IType type;

	public ASTFun(List<Parameter> parameters, IASTNode body) {
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
		newEnv.endScope();

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// Start a new closure
		Closure closure = code.createClosure((FunType)type);
		code.emit_new(closure.name);
		code.emit_dup();
		code.emit_invokespecial(closure.name, "<init>", "()V");
		// Set closure's envc  
		StackFrame currentFrame = code.getCurrentFrame();
		if (currentFrame != null) {
			code.emit_comment("initialize closure's SL");
			code.emit_dup();
			code.emit_aload(code.getCurrentSP());
			code.emit_checkcast(currentFrame.name);
			code.emit_putfield(closure.name, "SL", closure.envc.toJasmin());
		}
		// Start a new frame because a function has its own scope
		StackFrame frame = code.createFrame();
		closure.setLocalEnv(frame);
		
		// Initialize parameters
		ICompilationEnvironment newEnv = env.beginScope();
		int location = 0;
		for (Parameter param : parameters) {
			newEnv.assoc(param.id, location++);
			frame.addLocation(code.toJasmin(param.type));
		}
		// Compile the function body
		code.pushFrame(frame);
		code.pushClosure(closure);
		body.compile(code, newEnv);
		code.popClosure();
		code.popFrame();
		
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
