package main;

import compiler.*;
import parser.ParseException;
import parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import ast.ASTNode;

public class Compiler {

  public static void main(String args[]) {
    Parser parser = new Parser(System.in);
    ASTNode exp;

    try {
      exp = parser.Start();

      CodeBlock code = new CodeBlock();
//      exp.compile(code);

      code.dump("DemoLab3.j");

    } catch (Exception e) {
      System.out.println ("Syntax Error!");
    }
  }
  
	public static void compile(String s) throws ParseException, FileNotFoundException {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		ASTNode n = parser.Start();
		
        CodeBlock code = new CodeBlock();
//		n.compile(code);
		
		code.dump("Demo.j");
	}

}
