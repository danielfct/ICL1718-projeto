package main;

import compiler.*;
import parser.ParseException;
import parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import ast.ASTNode;

public class Compiler {
	/*
	 * (Needed for compatibility with WindowsCompilerTests.java)
	 * 
	 * Parametrise it with the directory in which your jasmin.jar resides,
	 * relative to your project's top directory
	 * 
	 * e.g. My eclipse project is a directory called ICL with
	 * subdirectories notes, slides and labs. My labs folder
	 * has a subdirectory src, and jasmin.jar is inside src.
	 */
	private static final String dir = "./" + "labs/src/";

  public static void main(String args[]) {
    Parser parser = new Parser(System.in);
    ASTNode exp;

    try {
      exp = parser.Start();

      CodeBlock code = new CodeBlock();
//      exp.compile(code);

      code.dump(dir + "DemoLab3.j");

    } catch (Exception e) {
      System.out.println ("Syntax Error!");
    }
  }
  
	public static void compile(String s) throws ParseException, FileNotFoundException {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		ASTNode n = parser.Start();
		
        CodeBlock code = new CodeBlock();
//		n.compile(code);
		
		code.dump(dir + "Demo.j");
	}

}
