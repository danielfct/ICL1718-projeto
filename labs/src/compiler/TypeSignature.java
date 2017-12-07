package compiler;

import java.io.PrintStream;
import java.util.List;

import types.IType;

public class TypeSignature implements ITypeSignature {

	public final String name;
	public final List<String> params;
	public final IType ret;

	public TypeSignature(List<String> params, IType ret) {
		this.name = "TypeSignature_" + IdFactory.singleton.typeSignature();
		this.params = params;
		this.ret = ret;
	}

	@Override
	public void dump(PrintStream out, String returnType) {
		out.println(".interface public " + name);
		out.println(".super java/lang/Object");
		out.print(".method public abstract apply(");
		for (String s : params)
			out.print(s);
		out.println(")" + returnType);
		out.println(".end method");
	}
	
	@Override
	public String toJasmin() {
		return "L" + name + ";";
	}

}
