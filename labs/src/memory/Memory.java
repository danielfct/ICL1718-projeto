package memory;

import values.IValue;
import values.RefValue;

public class Memory implements MemoryManagement {
	
	public static final Memory singleton = new Memory();
	
	private Memory() { }
	
	@Override
	public RefValue var(IValue value) {
		return new MemoryCell(value);
	}

	@Override
	public IValue get(RefValue reference) {
		return ((MemoryCell) reference).value;
	}

	@Override
	public IValue set(RefValue reference, IValue value) {
		return ((MemoryCell) reference).value = value;
	}
	
}
