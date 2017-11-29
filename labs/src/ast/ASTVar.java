package ast;

import compiler.CodeBlock;
import compiler.Reference;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.Memory;
import types.IType;
import types.RefType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class ASTVar implements ASTNode {

	final ASTNode expression;
	private IType type;
	
	public ASTVar(ASTNode n) {
		this.expression = n;
		this.type = null;
	}
	
	
	@Override
	public String toString() {
		return "var(" + expression + ")";
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) 
			throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue v = expression.eval(env);
		return Memory.singleton.newVar(v);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) 
			throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType t = expression.typecheck(env);
		return type = new RefType(t);	
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) 
			throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_comment("Starting " + this);
		// Get or create a new Reference in the compiler
		IType type = expression.getType();
		Reference ref = code.requestReference(type);
		code.emit_new(ref.name);
		code.emit_dup();
		code.emit_invokespecial(ref.name, "<init>", "()V");
		code.emit_dup();
		expression.compile(code, env);
		// Associate the value on top of stack with the new reference
		code.emit_putfield(ref.name, "value", code.toJasmin(ref.type));
		code.emit_comment("Ending " + this);
	}

	@Override
	public IType getType() {
		return type;
	}

}
