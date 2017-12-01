package types;

import java.util.Collection;
import java.util.Objects;

public class FunType implements IType {

	public final Collection<IType> parameters;
	public final IType ret;

	public FunType(Collection<IType> parameters, IType ret) {
		this.parameters = parameters;
		this.ret = ret;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameters, ret);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof FunType && this.parameters.equals(((FunType) other).parameters)
				&& this.ret.equals(((FunType) other).ret);
	}

	@Override
	public String toString() {
		return "Function(" + parameters + " -> " + ret + ")"; // TODO
	}

	@Override
	public String toJasmin() {
		return "Ljava/lang/Object;";
	}

}
