package memory;

import values.IValue;
import values.RefValue;

public class MemoryCell implements RefValue {

	IValue value;

	public MemoryCell(IValue value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "var(" + value.toString() + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof MemoryCell && this.value.equals(((MemoryCell)other).value);
	}

}
