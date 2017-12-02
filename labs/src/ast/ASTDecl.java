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
import static compiler.CodeBlock.FRAME_SL;

public class ASTDecl implements ASTNode {

	static class Declaration {
		String id;
		ASTNode def;

		public Declaration(String id, ASTNode def) {
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

	List<Declaration> declarations;
	ASTNode body;
	private IType type;

	public ASTDecl() {
		this.declarations = new ArrayList<>();
		this.type = null;
	}

	public void addBody(ASTNode body) {
		this.body = body;
	}

	public void newBinding(String id, ASTNode e) {
		declarations.add(new Declaration(id, e));
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IValue> newenv = env.beginScope();
		for (Declaration d : declarations)
			newenv.assoc(d.id, d.def.eval(env));
		IValue value = body.eval(newenv);
		env.endScope();

		return value;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IType> newEnv = env.beginScope();
		for (Declaration d : declarations)
			newEnv.assoc(d.id, d.def.typecheck(env));
		type = body.typecheck(newEnv);
		env.endScope();

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		System.out.println("compiling: " + this);
		// Create a new StackFrame in the compiler
		code.emit_comment("decl");
		StackFrame frame = code.newFrame();
		code.emit_new(frame.name);
		code.emit_dup();
		code.emit_invokespecial(frame.name, "<init>", "()V");
		// Initialize Static Linker
		if (frame.ancestor != null) {
			code.emit_dup();
			code.emit_aload(FRAME_SL);
			code.emit_putfield(frame.name, "SL", frame.ancestor.toJasmin());
		}

		// x=1, x=y+1, x=decl y=1 in x+y end, etc
		ICompilationEnvironment newEnv = env.beginScope();
		for (Declaration d : declarations) {
			code.emit_comment(d.id);
			code.emit_dup();
			d.def.compile(code, env);
			int location = frame.nextLocation();
			newEnv.assoc(d.id, location);
			String type = code.toJasmin(d.def.getType());
			frame.setLocation(location, type);
			code.emit_putfield(frame.name, "loc_" + String.format("%02d", location), type);
		}
		code.setCurrentFrame(frame);
		code.emit_astore(FRAME_SL);
		code.emit_comment("in");
		body.compile(code, newEnv);

		code.emit_comment("end");
		env.endScope();
		code.endScope();
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
