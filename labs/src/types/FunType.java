package types;

public class FunType implements IType {

	public final IType parameterType;
	public final IType returnType;
	
	public FunType(IType parameterType, IType returnType) {
		this.parameterType = parameterType;
		this.returnType = returnType;
	}
	
	@Override
	public boolean equals(Object other) {	
		return other instanceof FunType && 
				this.parameterType.equals(((FunType) other).parameterType) &&
				this.returnType.equals(((FunType) other).returnType);
	}
	
	@Override
	public String toString() {
		return "Function";
	}

	@Override
	public String toJasmin() {
		return "Ljava/lang/Object;";
	}
	
}
