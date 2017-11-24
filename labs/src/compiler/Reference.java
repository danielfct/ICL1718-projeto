package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import main.Compiler;

public class Reference implements IReference {

	public final String name;
	private String type;

	public Reference(String name) {
		this(name, null);
	}	
	
	public Reference(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public void dump() throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(Compiler.dir + name + ".j"));
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println();
		out.println("; variables");
		out.println(".field public value " + type);
		out.println();
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("       aload_0");
		out.println("       invokenonvirtual java/lang/Object/<init>()V");
		out.println("       return");
		out.println(".end method");
		out.println();
		out.println("; override toString");
		out.println(".method public toString()Ljava/lang/String;");
		out.println("		ldc \"" + this + "\"");
		out.println("		areturn");
		out.println(".end method");
		out.close();
	}
	
	@Override
	public String toJasmin() {
		return "L" + name + ";";
	}
	
	@Override
	public String toString() {
		return name + " " + type;
	}

}