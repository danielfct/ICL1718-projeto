package compiler;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class StackFrame implements IStackFrame {

	public final String name;
	public final StackFrame ancestor;
	private final List<String> locations;

	public StackFrame(StackFrame ancestor) {
		this.name = "Frame_" + IdFactory.singleton.frame();
		this.ancestor = ancestor;
		this.locations = new LinkedList<String>();
	}
	
	@Override
	public void addLocation(String type) {
		locations.add(type);
	}

	@Override
	public void dump(PrintStream out) {
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println();
		out.println("; variables");
		if (ancestor != null)
			out.println(".field public SL " + ancestor.toJasmin());
		int loc = 0;
		for (String type : locations) {
			out.println(".field public loc_" + String.format("%02d", loc++) + " " + type);
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
			sb.append(ancestor.name + " -> ");
		sb.append(name);
		sb.append(" [");
		sb.append(String.join(", ", locations));
		sb.append("]");
		return sb.toString();
	}

}
