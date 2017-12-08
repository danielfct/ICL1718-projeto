package compiler;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public interface IClosure {

	void dump(PrintStream out) throws FileNotFoundException;	
	
	void setLocalEnv(StackFrame frame);
	
	String toJasmin();

}
