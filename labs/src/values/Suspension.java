package values;

import java.util.Objects;

import ast.IASTNode;
import environment.DuplicateIdentifierException;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;

public class Suspension implements IValue {

	private final IASTNode expression;
	private final IEnvironment<IValue> envs;
	private IValue value;
	
	public Suspension(IASTNode expression, IEnvironment<IValue> envs) {
		this.expression = expression;
		this.envs = envs;
		this.value = null;
	}
	
	public IValue evaluate() throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		if (value == null) {
			value = expression.eval(envs);
			if (value instanceof Suspension) // Recursive way to get rid of suspensions inside suspensions
				value = ((Suspension)value).evaluate();
		}
		return value;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(expression, envs, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Suspension))
			return false;
		Suspension other = (Suspension) obj;
		return Objects.equals(expression, other.expression) && Objects.equals(envs, other.envs) && Objects.equals(value, other.value);
	}
	
	@Override
	public String toString() {
		return "suspension(" + expression + ", " + value + ")";
	}
	
}
