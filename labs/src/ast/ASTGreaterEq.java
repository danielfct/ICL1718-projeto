package ast;

import compiler.CodeBlock;
import compiler.IdFactory;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.BoolType;
import types.IType;
import types.IntType;
import types.TypingException;
import values.BoolValue;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTGreaterEq implements ASTNode {

	final ASTNode left, right;
	private IType type;

	public ASTGreaterEq(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
		this.type = null;
	}

	@Override
	public String toString() {
		return left + " >= " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(env);
		IValue r = right.eval(env);
		
		if (l instanceof IntValue && r instanceof IntValue)
			return new BoolValue(((IntValue)l).getValue() >= ((IntValue)r).getValue());
		else
			throw new TypeMismatchException("Wrong types on Greater or Equal Operation: Ge(" + l + ", " + r + ")");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);
		
		if (l == IntType.singleton && r == IntType.singleton)
			type = BoolType.singleton;
		else
			throw new TypingException("Wrong types on Greater or Equal Operation: Ge(" + l + ", " + r + ")");
		
		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// if (value1 >= value2)
		//		jump to labelEqual
		//		push value true
		//	else
		//		push value false
		//		jump to labelExit

		String labelGreaterEq = "label" + IdFactory.singleton.label();
		String labelExit = "label" + IdFactory.singleton.label();

		left.compile(code, env);
		right.compile(code, env);
		code.emit_icmpge(labelGreaterEq);
		code.emit_bool(false);
		code.emit_jump(labelExit);
		code.emit_anchor(labelGreaterEq);
		code.emit_bool(true);
		code.emit_anchor(labelExit);
	}

	@Override
	public IType getType() {
		return type;
	}

}
