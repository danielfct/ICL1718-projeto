package types;

public class RefType implements IType {
	
	private IType type;
	
	public RefType(IType type) { 
		this.type = type; 
	}
	
	public IType getType() { 
		return type; 
	}
	
	@Override
	public String toString() {
		 // TODO mudar quando for feita a compilação dos nós imperativos
		return "Ref(" + type + ")";
	}
	
	@Override
	public boolean equals(Object other) {	
		return other instanceof RefType && this.type.equals(((RefType) other).getType());
	}
}
