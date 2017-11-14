package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.BoolType;
import types.IType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTAnd implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTAnd(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " && " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);

		if (l instanceof BoolValue && r instanceof BoolValue)
			return new BoolValue(((BoolValue)l).getValue() && ((BoolValue)r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Conjunction Operation: And(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);
		
		if (l == BoolType.singleton && r == BoolType.singleton)
			type = BoolType.singleton;
		else
			throw new TypingException("Wrong types on Conjunction Operation: And(" + l + ", " + r + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		/* if (left == 0 or right == 0)
			jump to labelFalse
			push value 0
		else
			push value 1
		*/

//		String labelFalse = code.labelFactory.getLabel();
//		String labelExit = code.labelFactory.getLabel();
//		
//		left.compile(code);			
//		code.emit_ifeq(labelFalse);	
//		right.compile(code);		 					
//		code.emit_ifeq(labelFalse);
//		code.emit_bool(true);	
//		code.emit_jump(labelExit);	
//		code.emit_anchor(labelFalse);
//		code.emit_bool(false);	
//		code.emit_anchor(labelExit);
		
		left.compile(code, env);
		right.compile(code, env);
		code.emit_and();
	}

	@Override
	public IType getType() {
		return type;
	}

}
