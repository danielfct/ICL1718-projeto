package compiler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class StackFrame implements IStackFrame {

	public final String name;
	public final StackFrame ancestor;
	private List<String> locations; // e.g. posicao 0 com um valor do tipo I (inteiro) = loc_00 I
									//		posicao 1 com um valor do tipo Z (booleano) = loc_01 Z

	public StackFrame(StackFrame ancestor) {
		this.name = "Frame_" + IdFactory.singleton.frame();
		this.ancestor = ancestor;
		this.locations = new ArrayList<String>(5);
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
	public void dump(PrintStream out) {
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println();
		out.println("; variables");
		if (ancestor != null) 
			out.println(".field public SL " + ancestor.toJasmin());
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
	}

	@Override
	public String toJasmin() {
		return "L" + name + ";";
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (ancestor != null)
			sb.append(ancestor.name + " <- ");
		sb.append(name).append("\n");
		int index = 0;
		for (String type : locations)
			sb.append(String.format("%02d", index++) + " " + type).append("\n");
		
		return sb.toString();
	}
	
}
