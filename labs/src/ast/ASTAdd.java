package ast;

import util.IEnvironment;
import values.IValue;

public class ASTAdd implements ASTNode {

	ASTNode left, right;

	public ASTAdd(ASTNode l, ASTNode r) {
		left = l;
		right = r;
	}

	@Override
	public String toString() {
		return left.toString() + " + " + right.toString();
	}

	@Override
	public int eval(IEnvironment<IValue> env) {
		return left.eval(env)+right.eval(env);
	}
}
