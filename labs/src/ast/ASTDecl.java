package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

public class ASTDecl extends ASTNode {

	static class Declaration {
		final String id;
		final IASTNode def;

		public Declaration(String id, IASTNode def) {
			this.id = id;
			this.def = def;
		}

		@Override
		public String toString() {
			return id + " = " + def.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, def);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Declaration))
				return false;
			Declaration other = (Declaration) obj;
			return Objects.equals(id, other.id) && Objects.equals(def, other.def);
		}
	}

	final List<Declaration> declarations;
	private IASTNode body;
	private IType type;

	public ASTDecl() {
		this.declarations = new ArrayList<>();
		this.type = null;
	}

	public void setBody(IASTNode body) {
		this.body = body;
	}
	
	public IASTNode getBody() {
		return body;
	}

	public void newBinding(String id, IASTNode e) {
		declarations.add(new Declaration(id, e));
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IValue> newEnv = env.beginScope();
		for (Declaration decl : declarations)
			newEnv.assoc(decl.id, force(decl.def.eval(env)));
		IValue value = force(body.eval(newEnv));
		newEnv.endScope();

		return value;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IType> newEnv = env.beginScope();
		for (Declaration decl : declarations)
			newEnv.assoc(decl.id, decl.def.typecheck(env));
		type = body.typecheck(newEnv);
		newEnv.endScope();

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_comment("decl");
		StackFrame frame = code.createFrame();
		code.emit_new(frame.name);
		code.emit_dup();
		code.emit_invokespecial(frame.name, "<init>", "()V");
		if (frame.ancestor != null) {
			// Initialize Static Linker
			code.emit_dup();
			code.emit_aload(code.getCurrentSP());
			code.emit_putfield(frame.name, "SL", frame.ancestor.toJasmin());
		}
		ICompilationEnvironment newEnv = env.beginScope();
		int location = 0;
		for (Declaration d : declarations) {
			code.emit_comment("initialize constant " + d.id);
			code.emit_dup();
			d.def.compile(code, env);
			newEnv.assoc(d.id, location);
			String type = code.toJasmin(d.def.getType());
			frame.addLocation(type);
			code.emit_putfield(frame.name, "loc_" + location++, type);
		}
		
		code.pushFrame(frame);
		code.emit_astore(code.getCurrentSP());
		code.emit_comment("in");
		body.compile(code, newEnv);

		code.emit_comment("end");
		StackFrame currentFrame = code.getCurrentFrame();
		if (currentFrame.ancestor != null) {
			code.emit_aload(code.getCurrentSP());
			code.emit_checkcast(currentFrame.name);
			code.emit_getfield(currentFrame.name, "SL", currentFrame.ancestor.toJasmin());
		} else {
			code.emit_null();
		}
		code.emit_astore(code.getCurrentSP());
		code.pushFrame(currentFrame.ancestor);
		newEnv.endScope();
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(declarations, body);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTDecl))
			return false;
		ASTDecl other = (ASTDecl) obj;
		return Objects.equals(declarations, other.declarations) && Objects.equals(body, other.body);
	}
	
	@Override
	public String toString() {
		return "decl " + String.join(", ", declarations.stream().map(Declaration::toString).collect(Collectors.toList())) + " in " + body + " end ";
	}

}
