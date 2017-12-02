package types;

import java.util.Collection;
import java.util.Objects;

public class FunType implements IType {

	public final Collection<IType> paramsType;
	private IType retType;

	public FunType(Collection<IType> paramsType) {
		this(paramsType, null);
	}
	
	public FunType(Collection<IType> paramsType, IType ret) {
		this.paramsType = paramsType;
		this.retType = ret;
	}
	
	public IType getRetType() {
		return retType;
	}
	
	@Override
	public String toJasmin() {
		return "Ljava/lang/Object;";
	}

	
	@Override
	public String toString() {
		return "Function(" + paramsType + " -> " + retType + ")"; // TODO
	}

	@Override
	public int hashCode() {
		return Objects.hash(paramsType, retType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FunType))
			return false;
		FunType other = (FunType) obj;
		return Objects.equals(paramsType, other.paramsType) && Objects.equals(retType, other.retType);
	}

	
}
