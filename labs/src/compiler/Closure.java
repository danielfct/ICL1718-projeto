package compiler;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import types.FunType;
import types.IType;

public class Closure implements IClosure {

	public final String name;
	public final StackFrame env;
	public final TypeSignature signature;
	public final int SL;
	List<String> body;
	private final CodeBlock code;

	public Closure(StackFrame env, TypeSignature signature, CodeBlock code) {
		this.name = "Closure_" + IdFactory.singleton.closure();
		this.env = env;
		this.signature = signature;
		this.body = new ArrayList<String>(100);
		this.SL = signature.params.size() + 1;
		this.code = code;
	}

	@Override
	public void dump(PrintStream out, String resultType) throws FileNotFoundException {
		dumpHeader(out);
		dumpBody(out);
		dumpFooter(out);
	}

	//// fun x -> x + 1 end (1)

	// interface Type_00 { int call (int); }

	// class Closure_00 implements Type_00 { int call(int x) { return x+1; } }

	// .source type_00.j
	// .interface public type_00
	// .super java/lang/Object
	// .method public abstract call(I)I
	// .end method

	// .class public closure_01
	// .super java/lang/Object
	// .implements type_00
	// .field public SL Lframe_1;
	// .method public call(I)I
	// .limit locals 3
	// .limit stack 256
	// ; initialize new stackframe frame_1
	// new frame_1
	// dup
	// invokespecial frame_1/<init>()V
	// dup
	// iload 1
	// putfield frame_1/loc_00 I
	// astore 2

	// decl x = 1 in fun x:int => x+1 end (1) end
	// new frame_1
	// dup
	// invokespecial frame_1/<init>()V
	// dup
	// sipush 1
	// putfield frame_1/loc_00 I
	// astore 1 ; SP is now at local variable 1, 0 is reserved for “this”
	// new closure_01
	// dup
	// invokespecial closure_01/<init>()V
	// dup
	// aload 1 ; SP
	// putfield closure_01/SL Lframe_1;
	// checkcast type_00
	// sipush 1
	// invokeinterface type_00/call(I)I 2

	private void dumpHeader(PrintStream out) {
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println(".implements " + signature.name);
		out.println();
		out.println("; variables");
		out.println(".field public SL " + env.toJasmin());
		out.println();
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("	aload_0");
		out.println("	invokenonvirtual java/lang/Object/<init>()V");
		out.println("	return");
		out.println(".end method");
		out.println();
		out.println("; define call method");
		out.println(".method public call" + signature.toJasmin());
		out.println("	.limit locals 5");
		out.println("	.limit stack 256");
	}

	private void dumpBody(PrintStream out) {
		for (String s : body)
			out.println("	" + s);
	}

	private void dumpFooter(PrintStream out) {
		out.println(".end method");
		out.println();
		out.println("; define toString method");
		out.println(".method public toString()Ljava/lang/String;");
		out.println("	ldc \"" + this + "\"");
		out.println("	areturn");
		out.println(".end method");
	}

	@Override
	public StackFrame newFrame() {
		return code.newFrame();
	}

	@Override
	public void setCurrentFrame(StackFrame frame) {
		code.setCurrentFrame(frame);
	}

	@Override
	public StackFrame getCurrentFrame() {
		return code.getCurrentFrame();
	}

	@Override
	public Reference requestReference(IType type) {
		return code.requestReference(type);
	}

	@Override
	public Reference getReference(IType type) {
		return code.getReference(type);
	}

	@Override
	public TypeSignature getSignature(FunType type) {
		return code.getSignature(type);
	}

	@Override
	public Closure newClosure(FunType funType) {
		return code.newClosure(funType);
	}

	@Override
	public void endScope() {
		code.endScope();
	}

	@Override
	public String toJasmin(IType t) {
		return code.toJasmin(t);
	}

	@Override
	public void loadSP() {
		StackFrame currentFrame = code.getCurrentFrame();
		if (currentFrame != null) {
			emit_aload(SL);
			emit_checkcast(currentFrame.name);
		}
	}
	
	@Override
	public void emit_comment(String comment) {
		body.add("; " + comment);
	}
	
	@Override
	public void emit_newline() {
		body.add("\n");
	}
	
	@Override
	public void emit_new(String classname) {
		body.add("new " + classname);
	}

	@Override
	public void emit_invokespecial(String classname, String methodname, String descriptor) {
		body.add("invokespecial " + classname + "/" + methodname + descriptor);
	}
	
	@Override
	public void emit_invokeinterface(String interfacename, String methodname, String descriptor) {
		body.add("invokeinterface " + interfacename + "/" + methodname + descriptor);
	}

	@Override
	public void emit_null() {
		body.add("aconst_null");
	}

	@Override
	public void emit_getfield(String classname, String fieldname, String descriptor) {
		body.add("getfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_putfield(String classname, String fieldname, String descriptor) {
		body.add("putfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_dup() {
		body.add("dup");
	}

	@Override
	public void emit_astore(int n) {
		body.add("astore_" + n);
	}

	@Override
	public void emit_aload(int n) {
		body.add("aload_" + n);
	}

	@Override
	public void emit_checkcast(String t) {
		body.add("checkcast " + t);
	}

	@Override
	public void emit_push(int n) {
		body.add("sipush " + n);
	}

	@Override
	public void emit_add() {
		body.add("iadd");
	}

	@Override
	public void emit_mul() {
		body.add("imul");
	}

	@Override
	public void emit_div() {
		body.add("idiv");
	}

	@Override
	public void emit_sub() {
		body.add("isub");
	}

	@Override
	public void emit_xor() {
		body.add("ixor");
	}

	@Override
	public void emit_bool(boolean val) {
		if (val)
			emit_val(1);
		else
			emit_val(0);
	}

	@Override
	public void emit_val(int val) {
		if (val == -1)
			body.add("iconst_m1");
		else
			body.add("iconst_" + val);
	}

	@Override
	public void emit_icmpeq(String label) {
		body.add("if_icmpeq " + label);
	}

	@Override
	public void emit_icmpne(String label) {
		body.add("if_icmpne " + label);
	}

	@Override
	public void emit_icmpge(String label) {
		body.add("if_icmpge " + label);
	}

	@Override
	public void emit_icmpgt(String label) {
		body.add("if_icmpgt " + label);
	}

	@Override
	public void emit_icmple(String label) {
		body.add("if_icmple " + label);
	}

	@Override
	public void emit_icmplt(String label) {
		body.add("if_icmplt " + label);
	}

	@Override
	public void emit_ifeq(String label) {
		body.add("ifeq " + label);
	}

	@Override
	public void emit_ifne(String label) {
		body.add("ifne " + label);
	}

	@Override
	public void emit_jump(String label) {
		body.add("goto " + label);
	}

	@Override
	public void emit_anchor(String label) {
		body.add(label + ":");
	}

	@Override
	public void emit_and() {
		body.add("iand");
	}

	@Override
	public void emit_or() {
		body.add("ior");
	}

	@Override
	public void emit_pop() {
		body.add("pop");
	}

	@Override
	public void emit_swap() {
		body.add("swap");
	}

	@Override
	public void emit_iload(int position) {
		body.add("iload_" + position);
	}
	
	@Override
	public String toJasmin() {
		return "L" + name + ";";
	}

	@Override
	public String toString() {
		return name + "(" + signature + ", " + env + ")";
	}
	
}
