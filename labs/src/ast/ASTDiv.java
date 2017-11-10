package ast;

public class ASTDiv implements ASTNode {

	ASTNode left, right;

	public ASTDiv(ASTNode l, ASTNode r) {
		left = l;
		right = r;
	}

	@Override
	public String toString() {
		return left.toString() + " / " + right.toString();
	}

	@Override
	public int eval() {
		return left.eval() / right.eval();
	}
}
