package compiler;

import java.io.PrintStream;

public interface IStackFrame {

	void dump(PrintStream out);

	void addLocation(String type);
	
	String toJasmin();
	
}
