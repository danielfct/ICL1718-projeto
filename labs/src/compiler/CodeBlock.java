package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
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

	private final Deque<List<String>> codeStack;
	private final Deque<Integer> SPStack;
	private final List<StackFrame> frames;
	private final Map<IType, Reference> references;
	private final Map<FunType, TypeSignature> typeSignatures;
	private final List<Closure> closures;
	private StackFrame currentFrame;

	public CodeBlock() {
		this.codeStack = new ArrayDeque<List<String>>(5);
		codeStack.add(new LinkedList<String>()); // add the main code
		this.SPStack = new ArrayDeque<Integer>(5);
		SPStack.push(1); // main SP is stored at position 1 because it is static (no "this") and has 1 argument (array of strings)
		this.frames = new LinkedList<StackFrame>();
		this.references = new HashMap<IType, Reference>(10);
		this.typeSignatures = new HashMap<FunType, TypeSignature>(10);
		this.closures = new LinkedList<Closure>();
		this.currentFrame = null;
	}

	private void dumpHeader(PrintStream out) {
		out.println(".class public Main");
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
		for (String s : getCurrentCode())
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
			reference.dump(out);
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
			closure.dump(out);
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
	public StackFrame createFrame() {
		// Create a new StackFrame in the compiler
		StackFrame frame = new StackFrame(currentFrame);
		frames.add(frame);

		return frame;
	}

	@Override
	public void pushFrame(StackFrame frame) {
		currentFrame = frame;
	}

	@Override
	public void popFrame() {
		currentFrame = currentFrame.ancestor;
	}

	@Override
	public StackFrame getCurrentFrame() {
		return currentFrame;
	}

	@Override
	public void pushClosure(Closure closure) {
		codeStack.push(closure.body);
		SPStack.push(closure.SPPosition);
	}

	@Override
	public void popClosure() {
		codeStack.pop();
		SPStack.pop();
	}

	@Override
	public List<String> getCurrentCode() {
		return codeStack.peek();
	}

	@Override
	public Reference requestReference(IType type) {
		Reference reference = this.getReference(type);
		if (reference == null) {
			reference = new Reference(toJasmin(type));
			references.put(type, reference);
		}
		return reference;
	}

	@Override
	public Reference getReference(IType type) {
		return references.get(type);
	}

	private TypeSignature requestSignature(FunType funType) {
		TypeSignature signature = getSignature(funType);
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
	public Closure createClosure(FunType funType) {
		TypeSignature signature = this.requestSignature(funType);
		Closure closure = new Closure(currentFrame, signature);
		closures.add(closure);
		return closure;
	}

	@Override
	public int getCurrentSP() {
		return SPStack.peek();
	}

	@Override
	public String toJasmin(IType t) {
		if (t == IntType.singleton)
			return ((IntType) t).toJasmin();
		else if (t == BoolType.singleton)
			return ((BoolType) t).toJasmin();
		else if (t instanceof RefType)
			return requestReference(((RefType) t).type).toJasmin();
		else if (t instanceof FunType)
			return requestSignature((FunType) t).toJasmin();
		else
			throw new RuntimeException("Tojasmin not implemented for type " + t);

	}

	// Bytecode instructions

	@Override
	public void emit_comment(String comment) {
		getCurrentCode().add("; " + comment);
	}

	@Override
	public void emit_newline() {
		getCurrentCode().add("\n");
	}

	@Override
	public void emit_new(String classname) {
		getCurrentCode().add("new " + classname);
	}

	@Override
	public void emit_invokespecial(String classname, String methodname, String descriptor) {
		getCurrentCode().add("invokespecial " + classname + "/" + methodname + descriptor);
	}

	@Override
	public void emit_invokeinterface(String interfacename, String methodname, String descriptor, int nArgs) {
		getCurrentCode().add("invokeinterface " + interfacename + "/" + methodname + descriptor + " " + nArgs);
	}

	@Override
	public void emit_null() {
		getCurrentCode().add("aconst_null");
	}

	@Override
	public void emit_getfield(String classname, String fieldname, String descriptor) {
		getCurrentCode().add("getfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_putfield(String classname, String fieldname, String descriptor) {
		getCurrentCode().add("putfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_dup() {
		getCurrentCode().add("dup");
	}

	@Override
	public void emit_astore(int n) {
		getCurrentCode().add("astore_" + n);
	}

	@Override
	public void emit_aload(int n) {
		getCurrentCode().add("aload_" + n);
	}

	@Override
	public void emit_checkcast(String t) {
		getCurrentCode().add("checkcast " + t);
	}

	@Override
	public void emit_push(int n) {
		getCurrentCode().add("sipush " + n);
	}

	@Override
	public void emit_add() {
		getCurrentCode().add("iadd");
	}

	@Override
	public void emit_mul() {
		getCurrentCode().add("imul");
	}

	@Override
	public void emit_div() {
		getCurrentCode().add("idiv");
	}

	@Override
	public void emit_sub() {
		getCurrentCode().add("isub");
	}

	@Override
	public void emit_xor() {
		getCurrentCode().add("ixor");
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
			getCurrentCode().add("iconst_m1");
		else
			getCurrentCode().add("iconst_" + val);
	}

	@Override
	public void emit_icmpeq(String label) {
		getCurrentCode().add("if_icmpeq " + label);
	}

	@Override
	public void emit_icmpne(String label) {
		getCurrentCode().add("if_icmpne " + label);
	}

	@Override
	public void emit_icmpge(String label) {
		getCurrentCode().add("if_icmpge " + label);
	}

	@Override
	public void emit_icmpgt(String label) {
		getCurrentCode().add("if_icmpgt " + label);
	}

	@Override
	public void emit_icmple(String label) {
		getCurrentCode().add("if_icmple " + label);
	}

	@Override
	public void emit_icmplt(String label) {
		getCurrentCode().add("if_icmplt " + label);
	}

	@Override
	public void emit_ifeq(String label) {
		getCurrentCode().add("ifeq " + label);
	}

	@Override
	public void emit_ifne(String label) {
		getCurrentCode().add("ifne " + label);
	}

	@Override
	public void emit_jump(String label) {
		getCurrentCode().add("goto " + label);
	}

	@Override
	public void emit_anchor(String label) {
		getCurrentCode().add(label + ":");
	}

	@Override
	public void emit_and() {
		getCurrentCode().add("iand");
	}

	@Override
	public void emit_or() {
		getCurrentCode().add("ior");
	}

	@Override
	public void emit_pop() {
		getCurrentCode().add("pop");
	}

	@Override
	public void emit_swap() {
		getCurrentCode().add("swap");
	}

	@Override
	public void emit_iload(int position) {
		getCurrentCode().add("iload_" + position);
	}

}
