package types;

public class RefType implements IReferenceType {
	
	private IType type;
	
	public RefType(IType type) { 
		this.type = type; 
	}
	
	public IType getType() { 
		return type; 
	}
	
	@Override
	public boolean equals(Object other) {	
		return other instanceof RefType && this.type.equals(((RefType) other).getType());
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
