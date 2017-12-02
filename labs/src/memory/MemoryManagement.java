package memory;

import values.IValue;
import values.IRefValue;

public interface MemoryManagement {

	IRefValue newVar(IValue value);

	IValue get(IRefValue reference);

	IValue set(IRefValue reference, IValue value);

	// void free(RefValue reference); // Unnecessary because java gc does it for us

}
