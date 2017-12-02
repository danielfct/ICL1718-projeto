package compiler;

import java.io.PrintStream;

public interface IStackFrame {

	void dump(PrintStream out);

	String toJasmin();

	int nextLocation();

	void setLocation(int location, String type);
	
	void addLocation(String type);

}
