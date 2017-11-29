package compiler;

import java.io.PrintStream;

public class TypeSignature implements ITypeSignature {

	public final String name;
	public final String parameterType;
	public final String returnType;
	
	public TypeSignature(String parameterType, String returnType) {
		this.name = "Type_" + IdFactory.singleton.typeSignature();
		this.parameterType = parameterType;
		this.returnType = returnType;
	}
	
	@Override
	public void dump(PrintStream out) {
		out.println(".interface " + name);
		out.println(".super java/lang/Object");
		out.println(".method public abstract call(" + parameterType + ")" + returnType);
		out.println(".end method");
	}
	
	@Override
	public String toString() {
		return parameterType + "->" + returnType;
	}

}
