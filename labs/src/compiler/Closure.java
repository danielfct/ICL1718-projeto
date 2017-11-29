package compiler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class Closure implements IClosure {

	public final int id;
	public final String name;
	public final TypeSignature signature;
	private List<String> code;
	private StackFrame env;
	
	public Closure(StackFrame env, TypeSignature signature) {
		this.id = IdFactory.singleton.closure();
		this.name = "Closure_" + id;
		this.env = env;
		this.signature = signature;
		this.code = new ArrayList<String>(100);
	}
	
	@Override
	public void dump(PrintStream out) {
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println(".implements " + signature.name);
		out.println(".field public SL " + env.toJasmin());
		out.println("; define call method");
		out.println(".method public call(" + signature.parameterType + ")" + signature.returnType);
//		.limit locals (nArgs + 1)
		out.println(".limit stack 256");
		for (String s : code)
			out.println("       " + s);
		out.println(".end method");
		out.println("; define toString method");
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
		return name + "(" + signature + ")";
	}
	

	
	
////fun x -> x + 1 end (1)
	
//	interface Type_00 { int call (int); }
	
//	class Closure_00 implements Type_00 { int call(int x) { return x+1; } }	

//	.source type_00.j
//	.interface public type_00
//	.super java/lang/Object
//	.method public abstract call(I)I
//	.end method
	
//	.class public closure_01
//	.super java/lang/Object
//	.implements type_00
//	.field public SL Lframe_1;
//	.method public call(I)I
//	.limit locals 3
//	.limit stack 256
//	; initialize new stackframe frame_1
//	new frame_1
//	dup
//	invokespecial frame_1/<init>()V
//	dup
//	iload 1
//	putfield frame_1/loc_00 I
//	astore 2
	
	
//	decl x = 1 in fun x:int => x+1 end (1) end
//			new frame_1
//			dup
//			invokespecial frame_1/<init>()V
//			dup
//			sipush 1
//			putfield frame_1/loc_00 I
//			astore 1 ; SP is now at local variable 1, 0 is reserved for “this”
//			new closure_01
//			dup
//			invokespecial closure_01/<init>()V
//			dup
//			aload 1 ; SP
//			putfield closure_01/SL Lframe_1;
//			checkcast type_00
//			sipush 1
//			invokeinterface type_00/call(I)I 2
	
	
	
	
}
