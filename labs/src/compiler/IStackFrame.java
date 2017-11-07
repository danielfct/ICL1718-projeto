package compiler;

import java.io.FileNotFoundException;

public interface IStackFrame {
	
	int nextLocation();
	
	void setLocation(int location, String type);
	
	void dump() throws FileNotFoundException;
	
}
