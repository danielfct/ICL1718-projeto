package compiler;

import java.io.FileNotFoundException;

public interface IReference {
	
	void dump() throws FileNotFoundException;
	
	String toJasmin();
	
}
