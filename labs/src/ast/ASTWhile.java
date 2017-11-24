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

public class ASTWhile implements ASTNode {

	final ASTNode condition;
	final ASTNode expression;
	private IType type;

	public ASTWhile(ASTNode condition, ASTNode expression) {
		this.condition = condition;
		this.expression = expression;
		this.type = null;
	}

	@Override
	public String toString() {
		return "while " + condition + " do " + expression + " end";
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue c = condition.eval(env);

		if (c instanceof BoolValue) {
			if (((BoolValue)c).getValue()) {
				expression.eval(env);
				return eval(env);
			}
			else
				return new BoolValue(false);
		}
		else
			throw new TypeMismatchException("Wrong type on While condition: While (" + c + ") do ...");

	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType c = condition.typecheck(env);
		IType e = expression.typecheck(env);

		if (c == BoolType.singleton)
			type = BoolType.singleton;
		else 
			throw new TypingException("Wrong type on While statement: While(" + c + ") do (" + e + ")");

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		String labelStart = code.labelFactory.getLabel();
		String labelExit = code.labelFactory.getLabel();
		
		code.emit_anchor(labelStart);
		condition.compile(code, env);
		code.emit_ifeq(labelExit);
		expression.compile(code, env);
		code.emit_pop();
		code.emit_jump(labelStart);
		code.emit_anchor(labelExit);
		code.emit_bool(false);
		
		// start:
		// E1
		// if_eq exit
		// E2
		// pop
		// goto start
		// exit
		// sipush false
	}

	@Override
	public IType getType() {
		return type;
	}

}
