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

public class ASTIfThenElse implements ASTNode {

	final ASTNode condition;
	final ASTNode ifExpression;
	final ASTNode elseExpression;
	private IType type;

	public ASTIfThenElse(ASTNode condition, ASTNode ifExpression, ASTNode elseExpression) {
		this.condition = condition;
		this.ifExpression = ifExpression;
		this.elseExpression = elseExpression;
		this.type = null;
	}

	@Override
	public String toString() {
		return "if " + condition + " then " + ifExpression + " else " + elseExpression + " end";
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue c = condition.eval(env);

		if (c instanceof BoolValue) {
			if (((BoolValue)c).getValue())
				return ifExpression.eval(env);
			else
				return elseExpression.eval(env);
		}
		else
			throw new TypeMismatchException("Wrong type on If then else condition: If (" + c + ") then ... else ...");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType c = condition.typecheck(env);
		IType i = ifExpression.typecheck(env);
		IType e = elseExpression.typecheck(env);

		if (c == BoolType.singleton && i == e)
			type = i;
		else
			throw new TypingException("Wrong type on If then else statement: If (" + c + ") then (" + i + ") else " + "(" + e + ")");

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		String labelFalse = code.labelFactory.getLabel();
		String labelExit = code.labelFactory.getLabel();

		condition.compile(code, env);			
		code.emit_ifeq(labelFalse);	
		ifExpression.compile(code, env);		 					
		code.emit_jump(labelExit);	
		code.emit_anchor(labelFalse);
		elseExpression.compile(code, env);	
		code.emit_anchor(labelExit);
		//[|E1|]
		//if_eq false
		//[|E2|]
		//goto exit
		//false:
		//	[|E3]
		//exit
	}

	@Override
	public IType getType() {
		return type;
	}

}
