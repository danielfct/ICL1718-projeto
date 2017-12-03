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
		newEnv.endScope();

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {

		// CALL:
		//		; initialize a new frame
		//		new Frame_2
		//		dup
		//		invokespecial Frame_2/<init>()V
		//		; set frame's SL
		//		dup
		//		aload 1
		//		putfield Frame_2/SL LFrame_1;
		//		; add x parameter to the frame
		//		dup
		//		iload 1
		//		putfield Frame_2/loc_00 I
		//		; store SL
		//		astore 2
		//IType retType = body.getType();
		Closure closure = code.newClosure((FunType)type);
		StackFrame frame = code.newFrame();
		closure.emit_newline();
		closure.emit_comment("start new frame");
		closure.emit_new(frame.name);
		closure.emit_dup();
		closure.emit_invokespecial(frame.name, "<init>", "()V");
		if (frame.ancestor != null) {
			closure.emit_comment("initialize SL");
			closure.emit_dup();
			closure.emit_aload(closure.SL);
			closure.emit_putfield(frame.name, "SL", frame.ancestor.toJasmin());
		}
		closure.emit_newline();
		// Initialize parameters
		ICompilationEnvironment newEnv = env.beginScope();
		int position = 1; // position 0 reserved for 'this'
		for (Parameter param : parameters) {
			closure.emit_comment("initialize param " + param.id);
			closure.emit_dup();
			closure.emit_iload(position++); // x argument
			int location = frame.nextLocation();
			newEnv.assoc(param.id, location);
			String type = code.toJasmin(param.type);
			frame.setLocation(location, type);
			closure.emit_putfield(frame.name, "loc_" + String.format("%02d", location), type);
		}
		code.setCurrentFrame(frame);
		closure.emit_astore(closure.SL);
		closure.emit_comment("->");
		body.compile(closure, newEnv);
		

		// MAIN:
		//		; initialize a new closure
		//		new closure_01
		//		dup
		//		invokespecial closure_01/<init>()V
		//		; set closure's SL
		//		dup
		//		aload 1 ; SP
		//		putfield closure_01/SL Lframe_1;

		code.emit_new(closure.name);
		code.emit_dup();
		code.emit_invokespecial(closure.name, "<init>", "()V");
		code.emit_dup();
		code.emit_aload(2);
		code.emit_putfield(closure.name, "SL", closure.env.toJasmin());
		
		
		newEnv.endScope();
		code.endScope();
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
