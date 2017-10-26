package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CodeBlock {

	static class StackFrame {
		void dump() {
//			frame_id.j
//
//			.class frame_id
//			.super java/lang/Object
//			.field public SL Lancestor_frame_id; 
//			.field public loc_00 type;
//			.field public loc_01 type;
//			..
//			.field public loc_n type;
//			.end method 
		}
	}

	ArrayList<String> code;
	ArrayList<StackFrame> frames;
	public final LabelFactory labelFactory = new LabelFactory();

	public CodeBlock() {
		code = new ArrayList<String>(100);
	}

	void dumpHeader(PrintStream out) {
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
		out.println("       ; place your bytecodes here");
		out.println("       ; START");
		out.println("");
	}
	
	void dumpCode(PrintStream out) {
		for (String s : code)
			out.println("       " + s);
	}

	void dumpFooter(PrintStream out) {
		out.println("       ; END");
		out.println("");
		out.println("");		
		out.println("       ; convert to String;");
		out.println("       invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
		out.println("       ; call println ");
		out.println("       invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
		out.println("");		
		out.println("       return");
		out.println("");		
		out.println(".end method");
	}
	
//	private void dumpFrames() {
//		for (Stackframe frame : frames)
//			frame.dump();
//	}

	public void dump(String filename) throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(filename));
		dumpHeader(out);
		dumpCode(out);
		dumpFooter(out);
//		dumpFrames();
	}

	// Bytecode instructions

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

	// compare is two integer values are equal, if so, jump to given label
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

}
