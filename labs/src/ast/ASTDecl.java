package ast;

import java.util.ArrayList;
import java.util.List;

import compiler.CodeBlock;
import compiler.StackFrame;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;
import static main.Compiler.SL;

public class ASTDecl implements ASTNode {

	static class Declaration {

		String id;
		ASTNode def;

		public Declaration(String id, ASTNode def) {
			this.id = id;
			this.def = def;
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
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// 			decl
		// Create a new StackFrame in the compiler
		StackFrame frame = code.newFrame();
		code.emit_comment("Initialize frame " + frame.id);
		code.emit_new("Frame_" + frame.id);
		code.emit_dup();
		code.emit_invokespecial("Frame_" + frame.id, "<init>", "()V");
		// Initialize Static Linker
		if (frame.ancestor != null) {
			code.emit_dup();
			code.emit_aload(SL);
			code.emit_putfield("Frame_" + frame.id, "SL", "LFrame_" + frame.ancestor.id + ";");
		}

		// 			x=1 , x=y+1, x=decl y=1 in x+y end, etc
		ICompilationEnvironment newEnv = env.beginScope(); 
		for (Declaration d : declarations) {
			code.emit_dup();
			d.def.compile(code, env);
			int location = frame.nextLocation();
			newEnv.assoc(d.id, location);
			String type = code.toJasmin(d.def.getType());
			frame.setLocation(location, type);
			code.emit_putfield("Frame_" + frame.id, "loc_" + String.format("%02d", location), type);
		}
		// 			in
		code.setCurrentFrame(frame);
		code.emit_astore(SL);
		body.compile(code, newEnv);

		// 			end
		env.endScope();
		code.emit_endscope();
	}

	@Override
	public IType getType() {
		return type;
	}

}
