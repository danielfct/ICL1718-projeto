package values;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import ast.ASTFun.Parameter;
import ast.ASTNode;
import environment.IEnvironment;

public class ClosureValue implements IRefValue {

	public final Collection<Parameter> params;
	public final ASTNode body;
	public final IEnvironment<IValue> env;

	public ClosureValue(Collection<Parameter> params, ASTNode body, IEnvironment<IValue> env) {
		this.params = params;
		this.body = body;
		this.env = env;
	}
	
	@Override
	public String toString() {
		return "closure(fun " + String.join(", ", params.stream().map(Parameter::toString).collect(Collectors.toList()))
				+ " -> " + body + " end; " + env + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(params, body, env);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ClosureValue))
			return false;
		ClosureValue other = (ClosureValue) obj;
		return Objects.equals(params, other.params) &&
				Objects.equals(body, other.body) &&
				Objects.equals(env, other.env);
	}

}
