package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import main.Compiler;

public class StackFrame implements IStackFrame {

	public final int id;
	public final StackFrame ancestor;
	private List<String> locations; // e.g. posicao 0 com um valor do tipo I (inteiro) = loc_00 I
									//		posicao 1 com um valor do tipo Z (booleano) = loc_01 Z
	private final String filename;

	public StackFrame(int id, StackFrame ancestor) {
		this.id = id;
		this.ancestor = ancestor;
		this.locations = new ArrayList<String>(5);
		this.filename = "Frame_" + id + ".j";
	}

	@Override
	public int nextLocation() {
		return locations.size();
	}

	@Override
	public void setLocation(int location, String type) {
		if (location >= locations.size())
			locations.add(type);
		else
			locations.set(location, type);
	}

	@Override
	public void dump() throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(Compiler.dir + filename));
		out.println(".class Frame_" + id);
		out.println(".super java/lang/Object");
		out.println();
		out.println("; variables");
		if (ancestor != null) 
			out.println(".field public SL LFrame_" + ancestor.id + ";");
		int index = 0;
		for (String type : locations) {
			out.println(".field public loc_" + String.format("%02d", index++) + " " + type);
		}
		out.println();
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("       aload_0");
		out.println("       invokenonvirtual java/lang/Object/<init>()V");
		out.println("       return");
		out.println(".end method");
		out.close();
	}
	
}
