package compiler;

import java.io.PrintStream;
import java.util.Collection;

public class TypeSignature implements ITypeSignature {

	public final String name;
	public final Collection<String> params;
	public final String ret;

	public TypeSignature(Collection<String> params, String ret) {
		this.name = "Type_" + IdFactory.singleton.typeSignature();
		this.params = params;
		this.ret = ret;
	}

	@Override
	public void dump(PrintStream out) {
		out.println(".interface " + name);
		out.println(".super java/lang/Object");
		out.print(".method public abstract call(");
		for (String s : params)
			out.print(s);
		out.println(")" + ret);
		out.println(".end method");
	}

	@Override
	public String toString() {
		return params + "->" + ret;
	}

}
