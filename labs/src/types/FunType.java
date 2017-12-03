package types;

import java.util.List;
import java.util.Objects;

public class FunType implements IType {

	public final List<IType> paramsType;
	public final IType retType;
	
	public FunType(List<IType> paramsType, IType retType) {
		this.paramsType = paramsType;
		this.retType = retType;
	}
	
	@Override
	public String toJasmin() {
		return "Ljava/lang/Object;";
	}

	
	@Override
	public String toString() {
		return "Funt(" + paramsType + " -> " + retType + ")";
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
