package memory;

import values.IValue;
import values.IRefValue;

public class Memory implements MemoryManagement {

	public static final Memory singleton = new Memory();

	private Memory() {
	}

	@Override
	public IRefValue newVar(IValue value) {
		return new MemoryCell(value);
	}

	@Override
	public IValue get(IRefValue reference) {
		return ((MemoryCell) reference).value;
	}

	@Override
	public IValue set(IRefValue reference, IValue value) {
		return ((MemoryCell) reference).value = value;
	}

}
