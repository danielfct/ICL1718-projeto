package compiler;

import java.io.PrintStream;

public interface IReference {

	void dump(PrintStream out);

	String toJasmin();

}
