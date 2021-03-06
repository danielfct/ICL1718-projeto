package types;

public class IntType implements IType {

	public static final IntType singleton = new IntType();

	private IntType() {
	}

	@Override
	public String toString() {
		return "int";
	}

	@Override
	public String toJasmin() {
		return "I";
	}

}
