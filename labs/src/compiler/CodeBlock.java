package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.Compiler;
import types.IType;
import types.IntType;
import types.BoolType;
import types.FunType;
import types.RefType;

public class CodeBlock implements ICodeBuilder {

	public static final int MAIN_SL = 0;
	
	List<String> code;
	private List<StackFrame> frames;
	private Map<IType, Reference> references;
	private Map<FunType, TypeSignature> typeSignatures;
	private List<Closure> closures;
	private StackFrame currentFrame;

	public CodeBlock() {
		this.code = new ArrayList<String>(100);
		this.frames = new ArrayList<StackFrame>(10);
		this.references = new HashMap<IType, Reference>(10);
		this.typeSignatures = new HashMap<FunType, TypeSignature>(10);
		this.closures = new ArrayList<Closure>(10);
		this.currentFrame = null;
	}

	private void dumpHeader(PrintStream out) {
		out.println(".class public Demo");
		out.println(".super java/lang/Object");
		out.println("");
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("   aload_0");
		out.println("   invokenonvirtual java/lang/Object/<init>()V");
		out.println("   return");
		out.println(".end method");
		out.println("");
		out.println("; main method");
		out.println(".method public static main([Ljava/lang/String;)V");
		out.println("       ; set limits used by this method");
		out.println("       .limit locals 10");
		out.println("       .limit stack 256");
		out.println("");
		out.println("       ; setup local variables:");
		out.println("");
		out.println("       ;    1 - the PrintStream object held in java.lang.out");
		out.println("       getstatic java/lang/System/out Ljava/io/PrintStream;");
		out.println("");
		out.println("       ; START");
	}

	private void dumpCode(PrintStream out) {
		for (String s : code)
			out.println("       " + s);
	}

	private void dumpFooter(PrintStream out, String resultType) {
		out.println("       ; END");
		out.println("");
		out.println("");
		out.println("       ; convert to String;");
		out.println("       invokestatic java/lang/String/valueOf(" + resultType + ")Ljava/lang/String;");
		out.println("       ; call println ");
		out.println("       invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
		out.println("");
		out.println("       return");
		out.println("");
		out.println(".end method");
	}

	private void dumpFrames() throws FileNotFoundException {
		for (StackFrame frame : frames) {
			PrintStream out = new PrintStream(new FileOutputStream(Compiler.DIR + "/" + frame.name + ".j"));
			frame.dump(out);
			out.close();
		}
	}

	private void dumpReferences() throws FileNotFoundException {
		for (Reference reference : references.values()) {
			PrintStream out = new PrintStream(new FileOutputStream(Compiler.DIR + "/" + reference.name + ".j"));
			reference.dump(out, toJasmin(reference.type));
			out.close();
		}
	}

	private void dumpTypeSignatures() throws FileNotFoundException {
		for (TypeSignature signature : typeSignatures.values()) {
			PrintStream out = new PrintStream(new FileOutputStream(Compiler.DIR + "/" + signature.name + ".j"));
			signature.dump(out);
			out.close();
		}
	}

	private void dumpClosures() throws FileNotFoundException {
		for (Closure closure : closures) {
			PrintStream out = new PrintStream(new FileOutputStream(Compiler.DIR + "/" + closure.name + ".j"));
			closure.dump(out, ""); // TODO
			out.close();
		}
	}

	@Override
	public void dump(PrintStream out, String resultType) throws FileNotFoundException {
		dumpHeader(out);
		dumpCode(out);
		dumpFooter(out, resultType);
		dumpFrames();
		dumpReferences();
		dumpTypeSignatures();
		dumpClosures();
	}

	@Override
	public StackFrame newFrame() {
		// Create a new StackFrame in the compiler
		StackFrame frame = new StackFrame(currentFrame);
		frames.add(frame);

		return frame;
	}

	@Override
	public void setCurrentFrame(StackFrame frame) {
		currentFrame = frame;
	}

	@Override
	public StackFrame getCurrentFrame() {
		return currentFrame;
	}

	@Override
	public Reference requestReference(IType type) {
		Reference reference = this.getReference(type);
		if (reference == null) {
			reference = new Reference(type);
			references.put(type, reference);
		}
		return reference;
	}

	@Override
	public Reference getReference(IType type) {
		return references.get(type);
	}
	
	private TypeSignature requestSignature(FunType funType) {
		TypeSignature signature = this.getSignature(funType);
		if (signature == null) {
			signature = new TypeSignature(funType.paramsType.stream().map(this::toJasmin).collect(Collectors.toList()), toJasmin(funType.retType));
			typeSignatures.put(funType, signature);
		}
		return signature;
	}
	
	@Override
	public TypeSignature getSignature(FunType type) {
		return typeSignatures.get(type);
	}
	
	@Override
	public Closure newClosure(FunType funType) {
		TypeSignature signature = this.requestSignature(funType);
		Closure closure = new Closure(currentFrame, signature, this);
		closures.add(closure);
		return closure;
	}

	// TODO tentar melhorar
	@Override
	public String toJasmin(IType t) {
		if (t == IntType.singleton)
			return ((IntType) t).toJasmin();
		else if (t == BoolType.singleton)
			return ((BoolType) t).toJasmin();
		else if (t instanceof RefType)
			return references.get(((RefType) t).type).toJasmin();
		// else if (t instanceof FunType)
		// return closures.get((FunType)t).toJasmin();
		return null;
	}

	@Override
	public void loadSP() {
		if (currentFrame != null) {
			emit_aload(1);
			emit_checkcast(currentFrame.name);
		}
	}
	
	// Bytecode instructions
	
	@Override
	public void emit_comment(String comment) {
		code.add("; " + comment);
	}

	@Override
	public void emit_newline() {
		code.add("\n");
	}
	
	@Override
	public void emit_new(String classname) {
		code.add("new " + classname);
	}

	@Override
	public void emit_invokespecial(String classname, String methodname, String descriptor) {
		code.add("invokespecial " + classname + "/" + methodname + descriptor);
	}
	
	@Override
	public void emit_invokeinterface(String interfacename, String methodname, String descriptor, int nArgs) {
		code.add("invokeinterface " + interfacename + "/" + methodname + descriptor + " " + nArgs);
	}

	@Override
	public void emit_null() {
		code.add("aconst_null");
	}

	@Override
	public void emit_getfield(String classname, String fieldname, String descriptor) {
		code.add("getfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_putfield(String classname, String fieldname, String descriptor) {
		code.add("putfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_dup() {
		code.add("dup");
	}

	@Override
	public void emit_astore(int n) {
		code.add("astore_" + n);
	}

	@Override
	public void emit_aload(int n) {
		code.add("aload_" + n);
	}

	@Override
	public void emit_checkcast(String t) {
		code.add("checkcast " + t);
	}

	@Override
	public void emit_push(int n) {
		code.add("sipush " + n);
	}

	@Override
	public void emit_add() {
		code.add("iadd");
	}

	@Override
	public void emit_mul() {
		code.add("imul");
	}

	@Override
	public void emit_div() {
		code.add("idiv");
	}

	@Override
	public void emit_sub() {
		code.add("isub");
	}

	@Override
	public void emit_xor() {
		code.add("ixor");
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
			code.add("iconst_m1");
		else
			code.add("iconst_" + val);
	}

	@Override
	public void emit_icmpeq(String label) {
		code.add("if_icmpeq " + label);
	}

	@Override
	public void emit_icmpne(String label) {
		code.add("if_icmpne " + label);
	}

	@Override
	public void emit_icmpge(String label) {
		code.add("if_icmpge " + label);
	}
	
	@Override
	public void emit_icmpgt(String label) {
		code.add("if_icmpgt " + label);
	}

	@Override
	public void emit_icmple(String label) {
		code.add("if_icmple " + label);
	}

	@Override
	public void emit_icmplt(String label) {
		code.add("if_icmplt " + label);
	}

	@Override
	public void emit_ifeq(String label) {
		code.add("ifeq " + label);
	}

	@Override
	public void emit_ifne(String label) {
		code.add("ifne " + label);
	}

	@Override
	public void emit_jump(String label) {
		code.add("goto " + label);
	}

	@Override
	public void emit_anchor(String label) {
		code.add(label + ":");
	}

	@Override
	public void emit_and() {
		code.add("iand");
	}

	@Override
	public void emit_or() {
		code.add("ior");
	}

	@Override
	public void emit_pop() {
		code.add("pop");
	}

	@Override
	public void emit_swap() {
		code.add("swap");
	}
	
	@Override
	public void emit_iload(int position) {
		code.add("iload_" + position);
	}

}
