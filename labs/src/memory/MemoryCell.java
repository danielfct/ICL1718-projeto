package memory;

import values.IValue;
import values.RefValue;

public class MemoryCell implements RefValue {

	IValue value;

	public MemoryCell(IValue value) {
		this.value = value;
	}

}
