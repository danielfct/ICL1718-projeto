package types;

public class BoolType implements IType {
	
	public static final BoolType singleton = new BoolType();
	
	private BoolType() { }

	@Override
	public String toString() {
		return "Boolean";
	}
	
	@Override
	public String toJasmin() {
		return "Z";
	}
	
}
