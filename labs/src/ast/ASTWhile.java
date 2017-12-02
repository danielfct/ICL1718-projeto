package ast;

import java.util.Objects;

import compiler.ICodeBuilder;
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
			if (((BoolValue) c).getValue()) {
				expression.eval(env);
				return eval(env);
			} else {
				return new BoolValue(false);
			}
		} else {
			throw new TypeMismatchException("Wrong type on While condition: While (" + c + ") do ...");
		}
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
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		String labelStart = "label" + IdFactory.singleton.label();
		String labelExit = "label" + IdFactory.singleton.label();

		code.emit_anchor(labelStart);
		condition.compile(code, env);
		code.emit_ifeq(labelExit);
		expression.compile(code, env);
		code.emit_pop();
		code.emit_jump(labelStart);
		code.emit_anchor(labelExit);
		code.emit_bool(false);
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(condition, expression);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTWhile))
			return false;
		ASTWhile other = (ASTWhile) obj;
		return Objects.equals(condition, other.condition) && Objects.equals(expression, other.expression);
	}

}
