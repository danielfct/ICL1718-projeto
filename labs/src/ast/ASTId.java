package ast;

import util.IEnvironment;
import values.IValue;

public class ASTId implements ASTNode {
	String id;
	
	public ASTId(String id) {
		this.id = id;
	}

	@Override
	public int eval(IEnvironment<IValue> env) {
		// TODO Auto-generated method stub
		return 0;
	}
}
