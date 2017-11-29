package compiler;

import java.io.PrintStream;

public interface IClosure {

	void dump(PrintStream out);

	String toJasmin();
	
}
