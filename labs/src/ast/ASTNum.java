package ast;

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
	public int eval() {
		return val;
	}
}
