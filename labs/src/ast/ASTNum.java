package ast;

import util.IEnvironment;
import values.IValue;

public class ASTNum implements ASTNode {

	int val;

	public ASTNum(int n) {
		val = n;
	}

	@Override
	public String toString() {
		return Integer.toString(val);
	}

	@Override
	public int eval(IEnvironment<IValue> env) {
		return val;
	}
}
