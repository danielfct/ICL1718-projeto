package compiler;

import java.io.PrintStream;

import types.IType;

public class Reference implements IReference {

	public final String name;
	public final IType type;

	public Reference(IType type) {
		this.name = "Ref_" + IdFactory.singleton.reference();
		this.type = type;
	}

	@Override
	public void dump(PrintStream out, String fieldType) {
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println();
		out.println("; variables");
		out.println(".field public value " + fieldType);
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