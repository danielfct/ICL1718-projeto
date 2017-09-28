package values;

public class BoolValue implements IValue {

	private boolean value;

	public BoolValue(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof BoolValue && this.value == ((BoolValue) other).getValue();
	}

}