package ast;

import compiler.CodeBlock;
import types.BoolType;
import types.IType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.BoolValue;
import values.IValue;
import values.IntValue;
import values.TypeMismatchException;

public class ASTGreater implements ASTNode {

	final ASTNode left, right;

	public ASTGreater(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left + " > " + right;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = left.eval(null);
		IValue r = right.eval(null);
		
		if (l instanceof IntValue && r instanceof IntValue) {
			return new BoolValue(((IntValue)l).getValue() > ((IntValue)r).getValue());
		}
		else {
			throw new TypeMismatchException();
		}
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IType l = left.typecheck(null);
		IType r = right.typecheck(null);
		
		if (l == r)
			return BoolType.singleton;
		else
			throw new TypingException();
	}

	@Override
	public void compile(CodeBlock code) {
		// if (value1 > value2)
		//		jump to labelEqual
		//		push value true
		//	else
		//		push value false
		//		jump to labelExit

		String labelGreater = code.labelFactory.getLabel();
		String labelExit = code.labelFactory.getLabel();

		left.compile(code);
		right.compile(code);
		code.emit_icmpgt(labelGreater);
		code.emit_bool(false);
		code.emit_jump(labelExit);
		code.emit_anchor(labelGreater);
		code.emit_bool(true);
		code.emit_anchor(labelExit);
	}

}
