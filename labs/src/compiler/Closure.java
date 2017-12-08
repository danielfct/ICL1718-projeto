package compiler;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class Closure implements IClosure {

	public final String name;
	public final StackFrame envc;
	public final TypeSignature signature;
	public final int SPPosition;
	public final List<String> body;
	private StackFrame localEnv;
	
	public Closure(StackFrame envc, TypeSignature signature) {
		this.name = "Closure_" + IdFactory.singleton.closure();
		this.envc = envc;
		this.signature = signature;
		this.SPPosition = signature.params.size() + 1;
		this.body = new LinkedList<String>();
	}

	@Override
	public void dump(PrintStream out) throws FileNotFoundException {
		dumpHeader(out);
		dumpBody(out);
		dumpFooter(out);
	}

	private void dumpHeader(PrintStream out) {
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println(".implements " + signature.name);
		if (envc != null) {
			out.println();
			out.println("; variables");
			out.println(".field public SL " + envc.toJasmin());
		}
		out.println();
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("	aload_0");
		out.println("	invokenonvirtual java/lang/Object/<init>()V");
		out.println("	return");
		out.println(".end method");
		out.println();
		out.println("; define apply method");
		out.println(".method public apply" + signature);
		out.println("	.limit locals 10");
		out.println("	.limit stack 256");
		out.println("	; start new frame");
		out.println("	new " + localEnv.name);
		out.println("	dup");
		out.println("	invokespecial " + localEnv.name + "/<init>()V");
		if (localEnv.ancestor != null) {
			out.println("	; initialize frame SL");
			out.println("	dup");
			out.println("	aload_0");
			out.println("	getfield " + name + "/SL " + envc.toJasmin());
			out.println("	putfield " + localEnv.name + "/SL " + localEnv.ancestor.toJasmin());
		}
		int index = 0;
		for (String type : signature.params) {
			out.println("	; initialize param " + index);
			out.println(" 	dup");
			if (type.matches("L(.*);"))
				out.println("	aload_" + (index+1)); // +1 before "this" is stored at position 0
			else
				out.println(" 	iload_" + (index+1));
			out.println(" 	putfield " + localEnv.name + "/loc_" + String.format("%02d", index++) + " " + type);
		}
		out.println(" 	astore_" + SPPosition);
		out.println(" 	; ->");
	}

	private void dumpBody(PrintStream out) {
		for (String s : body)
			out.println("	" + s);
	}

	private void dumpFooter(PrintStream out) {
		out.println(" 	; end");
		if (localEnv.ancestor != null) {
			out.println(" 	aload_" + SPPosition);
			out.println(" 	checkcast " + localEnv.name);
			out.println(" 	getfield " + localEnv.name + "/SL " + localEnv.ancestor.toJasmin());
		} else {
			out.println(" 	aconst_null");
		}
		out.println(" 	astore_" + SPPosition);
		if (signature.ret.matches("L(.*);"))
			out.println("	areturn");
		else
			out.println("	ireturn");
		out.println(".end method");
		out.println();
		out.println("; define toString method");
		out.println(".method public toString()Ljava/lang/String;");
		out.println("	ldc \"" + this + "\"");
		out.println("	areturn");
		out.println(".end method");
	}

	@Override
	public void setLocalEnv(StackFrame frame) {
		this.localEnv = frame;
	}
	
	@Override
	public String toJasmin() {
		return "L" + name + ";";
	}

	@Override
	public String toString() {
		return name + "(" + signature + ", " + envc + ")";
	}

}
