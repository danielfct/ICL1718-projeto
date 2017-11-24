package types;

public class IntType implements IPrimitiveType {

	public static final IntType singleton = new IntType();
	
	private IntType() { }
	
	@Override
	public String toString() {
		return "Integer";
	}
	
	@Override
	public String toJasmin() {
		return "I";
	}
	
}
