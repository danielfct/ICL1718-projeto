package compiler;

import java.io.PrintStream;
import java.util.List;

public class TypeSignature implements ITypeSignature {

	public final String name;
	public final List<String> params;
	public final String ret;

	public TypeSignature(List<String> params, String ret) {
		this.name = "TypeSignature_" + IdFactory.singleton.typeSignature();
		this.params = params;
		this.ret = ret;
	}

	@Override
	public void dump(PrintStream out) {
		out.println(".interface public " + name);
		out.println(".super java/lang/Object");
		out.print(".method public abstract apply(");
		for (String s : params)
			out.print(s);
		out.println(")" + ret);
		out.println(".end method");
	}
	
	@Override
	public String toJasmin() {
		return "L" + name + ";";
	}
	
	@Override
	public String toString() {
		return "(" + String.join("", params) + ")" + ret;
	}

}
