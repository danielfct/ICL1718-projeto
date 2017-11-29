package types;

public class RefType implements IType {
	
	public final IType type;
	
	public RefType(IType type) { 
		this.type = type; 
	}
	
	@Override
	public boolean equals(Object other) {	
		return other instanceof RefType && this.type.equals(((RefType) other).type);
	}
	
	@Override
	public String toString() {
		return "Reference";
	}

	@Override
	public String toJasmin() {
		return "Ljava/lang/Object;";
	}
	
}
