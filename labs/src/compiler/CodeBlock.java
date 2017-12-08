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

	private final List<String> code;
	private final List<StackFrame> frames;
	private final Map<IType, Reference> references;
	private final Map<FunType, TypeSignature> typeSignatures;
	private final List<Closure> closures;
	private StackFrame currentFrame;
	private final Deque<Closure> closuresStack;

	public CodeBlock() {
		this.code = new LinkedList<String>();
		this.frames = new LinkedList<StackFrame>();
		this.references = new HashMap<IType, Reference>(10);
		this.typeSignatures = new HashMap<FunType, TypeSignature>(10);
		this.closures = new LinkedList<Closure>();
		this.currentFrame = null;
		this.closuresStack = new ArrayDeque<Closure>(5);
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
		closuresStack.push(closure);
	}

	@Override
	public void popClosure() {
		closuresStack.pop();
	}

	@Override
	public Closure getCurrentClosure() {
		return closuresStack.peek();
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
	public int getSPPosition() {
		// main SP is stored at position 1 because it is static (no "this") and has 1 argument (array of strings)
		return closuresStack.isEmpty() ? 1 : getCurrentClosure().SPPosition;
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
		if (closuresStack.isEmpty())
			code.add("; " + comment);
		else
			getCurrentClosure().body.add("; " + comment);
	}

	@Override
	public void emit_newline() {
		if (closuresStack.isEmpty())
			code.add("\n");
		else
			getCurrentClosure().body.add("\n");
	}

	@Override
	public void emit_new(String classname) {
		if (closuresStack.isEmpty())
			code.add("new " + classname);
		else
			getCurrentClosure().body.add("new " + classname);
	}

	@Override
	public void emit_invokespecial(String classname, String methodname, String descriptor) {
		if (closuresStack.isEmpty())
			code.add("invokespecial " + classname + "/" + methodname + descriptor);
		else
			getCurrentClosure().body.add("invokespecial " + classname + "/" + methodname + descriptor);
	}

	@Override
	public void emit_invokeinterface(String interfacename, String methodname, String descriptor, int nArgs) {
		if (closuresStack.isEmpty())
			code.add("invokeinterface " + interfacename + "/" + methodname + descriptor + " " + nArgs);
		else
			getCurrentClosure().body.add("invokeinterface " + interfacename + "/" + methodname + descriptor + " " + nArgs);
	}

	@Override
	public void emit_null() {
		if (closuresStack.isEmpty())
			code.add("aconst_null");
		else
			getCurrentClosure().body.add("aconst_null");
	}

	@Override
	public void emit_getfield(String classname, String fieldname, String descriptor) {
		if (closuresStack.isEmpty())
			code.add("getfield " + classname + "/" + fieldname + " " + descriptor);
		else
			getCurrentClosure().body.add("getfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_putfield(String classname, String fieldname, String descriptor) {
		if (closuresStack.isEmpty())
			code.add("putfield " + classname + "/" + fieldname + " " + descriptor);
		else
			getCurrentClosure().body.add("putfield " + classname + "/" + fieldname + " " + descriptor);
	}

	@Override
	public void emit_dup() {
		if (closuresStack.isEmpty())
			code.add("dup");
		else
			getCurrentClosure().body.add("dup");
	}

	@Override
	public void emit_astore(int n) {
		if (closuresStack.isEmpty())
			code.add("astore_" + n);
		else
			getCurrentClosure().body.add("astore_" + n);
	}

	@Override
	public void emit_aload(int n) {
		if (closuresStack.isEmpty())
			code.add("aload_" + n);
		else
			getCurrentClosure().body.add("aload_" + n);
	}

	@Override
	public void emit_checkcast(String t) {
		if (closuresStack.isEmpty())
			code.add("checkcast " + t);
		else
			getCurrentClosure().body.add("checkcast " + t);
	}

	@Override
	public void emit_push(int n) {
		if (closuresStack.isEmpty())
			code.add("sipush " + n);
		else
			getCurrentClosure().body.add("sipush " + n);
	}

	@Override
	public void emit_add() {
		if (closuresStack.isEmpty())
			code.add("iadd");
		else
			getCurrentClosure().body.add("iadd");
	}

	@Override
	public void emit_mul() {
		if (closuresStack.isEmpty())
			code.add("imul");
		else
			getCurrentClosure().body.add("imul");
	}

	@Override
	public void emit_div() {
		if (closuresStack.isEmpty())
			code.add("idiv");
		else
			getCurrentClosure().body.add("idiv");
	}

	@Override
	public void emit_sub() {
		if (closuresStack.isEmpty())
			code.add("isub");
		else
			getCurrentClosure().body.add("isub");
	}

	@Override
	public void emit_xor() {
		if (closuresStack.isEmpty())
			code.add("ixor");
		else
			getCurrentClosure().body.add("ixor");
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
		if (closuresStack.isEmpty()) {
			if (val == -1)
				code.add("iconst_m1");
			else
				code.add("iconst_" + val);
		}
		else {
			if (val == -1)
				getCurrentClosure().body.add("iconst_m1");
			else
				getCurrentClosure().body.add("iconst_" + val);
		}
	}

	@Override
	public void emit_icmpeq(String label) {
		if (closuresStack.isEmpty())
			code.add("if_icmpeq " + label);
		else
			getCurrentClosure().body.add("if_icmpeq " + label);
	}

	@Override
	public void emit_icmpne(String label) {
		if (closuresStack.isEmpty())
			code.add("if_icmpne " + label);
		else
			getCurrentClosure().body.add("if_icmpne " + label);
	}

	@Override
	public void emit_icmpge(String label) {
		if (closuresStack.isEmpty())
			code.add("if_icmpge " + label);
		else
			getCurrentClosure().body.add("if_icmpge " + label);
	}

	@Override
	public void emit_icmpgt(String label) {
		if (closuresStack.isEmpty())
			code.add("if_icmpgt " + label);
		else
			getCurrentClosure().body.add("if_icmpgt " + label);
	}

	@Override
	public void emit_icmple(String label) {
		if (closuresStack.isEmpty())
			code.add("if_icmple " + label);
		else
			getCurrentClosure().body.add("if_icmple " + label);
	}

	@Override
	public void emit_icmplt(String label) {
		if (closuresStack.isEmpty())
			code.add("if_icmplt " + label);
		else
			getCurrentClosure().body.add("if_icmplt " + label);
	}

	@Override
	public void emit_ifeq(String label) {
		if (closuresStack.isEmpty())
			code.add("ifeq " + label);
		else
			getCurrentClosure().body.add("ifeq " + label);
	}

	@Override
	public void emit_ifne(String label) {
		if (closuresStack.isEmpty())
			code.add("ifne " + label);
		else
			getCurrentClosure().body.add("ifne " + label);
	}

	@Override
	public void emit_jump(String label) {
		if (closuresStack.isEmpty())
			code.add("goto " + label);
		else
			getCurrentClosure().body.add("goto " + label);
	}

	@Override
	public void emit_anchor(String label) {
		if (closuresStack.isEmpty())
			code.add(label + ":");
		else
			getCurrentClosure().body.add(label + ":");
	}

	@Override
	public void emit_and() {
		if (closuresStack.isEmpty())
			code.add("iand");
		else
			getCurrentClosure().body.add("iand");
	}

	@Override
	public void emit_or() {
		if (closuresStack.isEmpty())
			code.add("ior");
		else
			getCurrentClosure().body.add("ior");
	}

	@Override
	public void emit_pop() {
		if (closuresStack.isEmpty())
			code.add("pop");
		else
			getCurrentClosure().body.add("pop");
	}

	@Override
	public void emit_swap() {
		if (closuresStack.isEmpty())
			code.add("swap");
		else
			getCurrentClosure().body.add("swap");
	}

	@Override
	public void emit_iload(int position) {
		if (closuresStack.isEmpty())
			code.add("iload_" + position);
		else
			getCurrentClosure().body.add("iload_" + position);
	}

}
