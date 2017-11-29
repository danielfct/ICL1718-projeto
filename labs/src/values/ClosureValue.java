package values;

import ast.ASTNode;
import environment.IEnvironment;

public class ClosureValue implements IRefValue {

	public final String parameter;
	public final ASTNode body;
	public final IEnvironment<IValue> env;
	
	public ClosureValue(String parameter, ASTNode body, IEnvironment<IValue> env) {
		this.parameter = parameter;
		this.body = body;
		this.env = env;
	}
	
	@Override
	public String toString() {
		return "closure(" + parameter + ", " + body + ", env)";
	}
	
}
