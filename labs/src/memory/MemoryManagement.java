package memory;

import values.IValue;
import values.RefValue;

public interface MemoryManagement {

	RefValue var(IValue value);
	
	IValue get(RefValue reference);
	
	IValue set(RefValue reference, IValue value);
	
//	void free(RefValue reference); // Unnecessary because java gc does it for us
	
}
