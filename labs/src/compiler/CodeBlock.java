package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Compiler;
import types.IType;
import types.IntType;
import types.BoolType;
import types.FunType;
import types.RefType;

import static main.Compiler.SL;

public class CodeBlock {
	
	private List<String> code;
	private List<StackFrame> frames;
	private Map<IType, Reference> references;
	private Map<FunType, TypeSignature> typeSignatures;
	private Map<FunType, Closure> closures;
	private StackFrame currentFrame;

	public CodeBlock() {
		this.code = new ArrayList<String>(100);
		this.frames = new ArrayList<StackFrame>(10);
		this.currentFrame = null;
		this.references = new HashMap<IType, Reference>(10);
		this.typeSignatures = new HashMap<FunType, TypeSignature>(10);
		this.closures = new HashMap<FunType, Closure>(10);
	}

	private void dumpHeader(PrintStream out) {
		out.println(".class public Demo");
		out.println(".super java/lang/Object");
		out.println("");
		out.println(";");
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("   aload_0");
		out.println("   invokenonvirtual java/lang/Object/<init>()V");
		out.println("   return");
		out.println(".end method");
		out.println("");
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
		for (Closure closure : closures.values()) {
			PrintStream out = new PrintStream(new FileOutputStream(Compiler.DIR + "/" + closure.name + ".j"));
			closure.dump(out);
			out.close();
		}
	}

	public void dump(PrintStream out, String resultType) throws FileNotFoundException {
		dumpHeader(out);
		dumpCode(out);
		dumpFooter(out, resultType);
		dumpFrames();
		dumpReferences();
		dumpTypeSignatures();
		dumpClosures();
	}

	public StackFrame newFrame() {
		// Create a new StackFrame in the compiler
		StackFrame frame = new StackFrame(currentFrame);
		frames.add(frame);

		return frame;
	}

	public void setCurrentFrame(StackFrame frame) {
		currentFrame = frame;
	}

	public StackFrame getCurrentFrame() {
		return currentFrame;
	}

	public Reference requestReference(IType type) {
		Reference reference = this.getReference(type);
		if (reference == null) {
			reference = new Reference(type);
			references.put(type, reference);
		}
		return reference;
	}

	public Reference getReference(IType type) {
		return references.get(type);
	}

	public void emit_endscope() {
		if (currentFrame.ancestor != null) {
			emit_aload(SL);
			emit_checkcast(currentFrame.name);
			emit_getfield(currentFrame.name, "SL", currentFrame.ancestor.toJasmin());
		}
		else {
			emit_null();
		}
		emit_astore(SL);
		setCurrentFrame(currentFrame.ancestor);
	}
	
	// TODO tentar melhorar
	public String toJasmin(IType t) {
		if (t == IntType.singleton)
			return ((IntType)t).toJasmin();
		else if (t == BoolType.singleton)
			return ((BoolType)t).toJasmin();
		else if (t instanceof RefType)
			return references.get(((RefType)t).type).toJasmin();
		else if (t instanceof FunType)
			return closures.get((FunType)t).toJasmin();
		return null;
	}

	// Bytecode instructions

	// make a comment
	public void emit_comment(String comment) {
		code.add("; " + comment);
	}

	// pushes stack pointer onto the operand stack 
	public void emit_SP() {
		if (currentFrame != null) {
			emit_aload(SL);
			emit_checkcast(currentFrame.name);
		}
	}

	// create new object
	public void emit_new(String classname) {
		code.add("new " + classname);
	}

	// invoke instance method; special handling for superclass, private, and instance initialization method invocations
	public void emit_invokespecial(String classname, String methodname, String descriptor) {
		code.add("invokespecial " + classname + "/" + methodname + descriptor);
	}

	public void emit_null() {
		code.add("aconst_null");
	}

	// getfield pops objectref (a reference to an object) from the stack, retrieves the value of the field identified by <classname/fieldname> 
	// from objectref, and pushes the one-word or two-word value onto the operand stack
	// Examples:
	// 		getfield Frame_n1/SL LFrame_n;
	// 		getfield Frame_1/loc_X I
	public void emit_getfield(String classname, String fieldname, String descriptor) {
		code.add("getfield " + classname + "/" + fieldname + " " + descriptor);
	}

	// putfield sets the value of the field identified by <classname/fieldname> in objectref (a reference to an object) 
	// to the single or double word value on the operand stack
	// Examples:
	// 		putfield Frame_n1/SL LFrame_n;
	// 		putfield Frame_1/loc_X I
	public void emit_putfield(String classname, String fieldname, String descriptor) {
		code.add("putfield " + classname + "/" + fieldname + " " + descriptor);
	}

	// This pops the top single-word value off the operand stack, and then pushes that value twice 
	//- i.e. it makes an extra copy of the top item on the stack
	public void emit_dup() {
		code.add("dup");
	}

	// Pops objectref (a reference to an object or array) off the stack and stores it in local variable <n>
	public void emit_astore(int n) {
		code.add("astore_" + n);
	}

	// retrieves an object reference from a local variable and pushes it onto the operand stack
	public void emit_aload(int n) {
		code.add("aload_" + n); 
	}

	// checks that the top item on the operand stack (a reference to an object or array) can be cast to a given type
	public void emit_checkcast(String t) {
		code.add("checkcast " + t);
	}

	// push value into the stack
	public void emit_push(int n) {
		code.add("sipush " + n);
	}

	// add the two top integer values on stack
	public void emit_add() {
		code.add("iadd");
	}

	// multiple the two top integer values on stack
	public void emit_mul() {
		code.add("imul");
	}

	// divide the two top integer values on stack
	public void emit_div() {
		code.add("idiv");
	}

	// subtract the two top integer values on stack
	public void emit_sub() {
		code.add("isub");
	}

	// xor between the two top integer values on stack
	public void emit_xor() {
		code.add("ixor");
	}

	// push 0 or 1 into the stack
	public void emit_bool(boolean val) {
		if (val)
			emit_val(1);
		else 
			emit_val(0);
	}

	// push a value from -1 to 5 into the stack
	public void emit_val(int val) {
		if (val == -1) 
			code.add("iconst_m1");
		else
			code.add("iconst_" + val);
	}

	// compare if two integer values are equal, if so, jump to given label
	public void emit_icmpeq(String label) {
		code.add("if_icmpeq " + label);
	}

	// compare if two integer values are not equal, if so, jump to given label
	public void emit_icmpne(String label) {
		code.add("if_icmpne " + label);
	}

	// compare if first integer value is greater or equal than second integer value, if so, jump to given label
	public void emit_icmpge(String label) {
		code.add("if_icmpge " + label);
	}

	// compare if first integer value is greater than second integer value, if so, jump to given label
	public void emit_icmpgt(String label) {
		code.add("if_icmpgt " + label);
	}

	// compare if first integer value is lesser or equal than second integer value, if so, jump to given label
	public void emit_icmple(String label) {
		code.add("if_icmple " + label);
	}

	// compare if first ineger value is lesser than second integer value, if so, jump to given label
	public void emit_icmplt(String label) {
		code.add("if_icmplt " + label);
	}

	// compare value on top of stack with 0, if equal, jumpo to given label
	public void emit_ifeq(String label) {
		code.add("ifeq " + label);
	}

	// check if value on top of stack is diferent than 0 
	public void emit_ifne(String label) {
		code.add("ifne " + label);
	}

	// jump to given label
	public void emit_jump(String label) {
		code.add("goto " + label);
	}

	// anchor the given label
	public void emit_anchor(String label) {
		code.add(label + ":");
	}

	// bitwise int AND
	public void emit_and() {
		code.add("iand");
	}

	// bitwise int OR
	public void emit_or() {
		code.add("ior");
	}
	
	// pop the value on top of the stack
	public void emit_pop() {
		code.add("pop");
	}
	
	// swap the top 2 values on stack
	public void emit_swap() {
		code.add("swap");
	}

}
