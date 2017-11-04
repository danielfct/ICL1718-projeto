package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.IntType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.BoolValue;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTGreaterEq implements ASTNode {

	final ASTNode left, right;

	public ASTGreaterEq(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
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
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IType l = left.typecheck(env);
		IType r = right.typecheck(env);
		
		if (l == IntType.singleton && r == IntType.singleton)
			return BoolType.singleton;
		else
			throw new TypingException("Wrong types on Greater or Equal Operation: Ge(" + l + ", " + r + ")");
	}

	@Override
	public void compile(CodeBlock code) {
		// if (value1 >= value2)
		//		jump to labelEqual
		//		push value true
		//	else
		//		push value false
		//		jump to labelExit

		String labelGreaterEq = code.labelFactory.getLabel();
		String labelExit = code.labelFactory.getLabel();

		left.compile(code);
		right.compile(code);
		code.emit_icmpge(labelGreaterEq);
		code.emit_bool(false);
		code.emit_jump(labelExit);
		code.emit_anchor(labelGreaterEq);
		code.emit_bool(true);
		code.emit_anchor(labelExit);
	}

}
