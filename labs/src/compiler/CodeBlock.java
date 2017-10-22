package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CodeBlock {
<<<<<<< HEAD

=======
	
>>>>>>> 011425bfc3abd7bae4a1a4f9a5c66cf3927df1e8
	ArrayList<String> code;

	public CodeBlock() {
		code = new ArrayList<String>(100);
	}

	public void emit_push(int n) {
		code.add("sipush "+n);
	}

	public void emit_add() {
		code.add("iadd");
	}

	public void emit_mul() {
		code.add("imul");
	}

	public void emit_div() {
		code.add("idiv");
	}

	public void emit_sub() {
		code.add("isub");
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

	void dumpCode(PrintStream out) {
<<<<<<< HEAD
		for (String s : code)
			out.println("       " + s);
	}

=======
		for( String s : code )
			out.println("       "+s);
	}
	
>>>>>>> 011425bfc3abd7bae4a1a4f9a5c66cf3927df1e8
	public void dump(String filename) throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(filename));
		dumpHeader(out);
		dumpCode(out);
		dumpFooter(out);
	}
}