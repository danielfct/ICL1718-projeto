package ast;

import java.util.ArrayList;

import util.IEnvironment;
import values.IValue;

public class ASTDecl implements ASTNode {
	static class Declaration {
		public Declaration(String id, ASTNode def) {
			this.id = id;
			this.def = def;
		}
		String id;
		ASTNode def;
	}
	
	ASTNode body;
	ArrayList<Declaration> decls;
	
	public ASTDecl() {
		decls = new ArrayList<>();
	}
	
	public void addBody(ASTNode body) {
		this.body = body;
	}
	
	public void newBinding(String id, ASTNode e) {
		decls.add(new Declaration(id,e));
	}

	@Override
	public int eval(IEnvironment<IValue> env) {
		// TODO Auto-generated method stub
		return 0;
	}
}
