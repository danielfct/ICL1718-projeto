package values;

public class IntValue implements IValue {
	
	private int value;
	
	public IntValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof IntValue && this.value == ((IntValue)other).getValue();
	}
	
}