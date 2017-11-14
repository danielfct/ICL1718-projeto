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
		// TODO
		return null;
	}
}
