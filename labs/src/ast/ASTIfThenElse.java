package ast;

import java.util.Objects;

import compiler.CodeBlock;
import compiler.IdFactory;
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
	final ASTNode thenExpression;
	final ASTNode elseExpression;
	private IType type;

	public ASTIfThenElse(ASTNode condition, ASTNode thenExpression, ASTNode elseExpression) {
		this.condition = condition;
		this.thenExpression = thenExpression;
		this.elseExpression = elseExpression;
		this.type = null;
	}

	@Override
	public String toString() {
		return "if " + condition + " then " + thenExpression + " else " + elseExpression + " end";
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue c = condition.eval(env);

		if (c instanceof BoolValue) {
			if (((BoolValue) c).getValue())
				return thenExpression.eval(env);
			else
				return elseExpression.eval(env);
		} else
			throw new TypeMismatchException("Wrong type on If then else condition: If (" + c + ") then ... else ...");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType c = condition.typecheck(env);
		IType i = thenExpression.typecheck(env);
		IType e = elseExpression.typecheck(env);

		if (c == BoolType.singleton && i.equals(e))
			type = i;
		else
			throw new TypingException("Wrong type on If then else statement: If (" + c + ") then (" + i + ") else " + "(" + e + ")");

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		String labelFalse = "label" + IdFactory.singleton.label();
		String labelExit = "label" + IdFactory.singleton.label();

		condition.compile(code, env);
		code.emit_ifeq(labelFalse);
		thenExpression.compile(code, env);
		code.emit_jump(labelExit);
		code.emit_anchor(labelFalse);
		elseExpression.compile(code, env);
		code.emit_anchor(labelExit);
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(condition, thenExpression, elseExpression);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTIfThenElse))
			return false;
		ASTIfThenElse other = (ASTIfThenElse) obj;
		return Objects.equals(condition, other.condition) && Objects.equals(thenExpression, other.thenExpression)
				&& Objects.equals(elseExpression, other.elseExpression);
	}

}
