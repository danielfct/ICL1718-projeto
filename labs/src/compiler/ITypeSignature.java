package compiler;

import java.io.PrintStream;

public interface ITypeSignature {

	void dump(PrintStream out);
	
	String toJasmin();

}
