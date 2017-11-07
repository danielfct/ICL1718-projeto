package main;

import compiler.*;
import parser.ParseException;
import parser.Parser;
import parser.TokenMgrError;
import types.TypingException;
import util.CompilationEnvironment;
import util.DuplicateIdentifierException;
import util.Environment;
import util.UndeclaredIdentifierException;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ast.ASTNode;

public class Compiler {

	public static final String dir = System.getProperty("user.dir")+"/src/";
	public static final int SL = 0;

	public static void main(String args[]) {
		try {
			compile(System.in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void compile(String program) throws FileNotFoundException {
		compile(new ByteArrayInputStream(program.getBytes()));	
	}
	
	public static void compile(InputStream is) throws FileNotFoundException {
		Parser parser = new Parser(is);
		try {
			ASTNode n = parser.Start();
			CodeBlock code = new CodeBlock();
			n.typecheck(new Environment<>());
			n.compile(code, new CompilationEnvironment());
			code.dump(dir + "Demo.j");
		} catch (TokenMgrError e) {
			System.out.println("Lexical Error!");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Syntax Error!");
			e.printStackTrace();
		} catch (TypingException e) {
			System.out.println("Typing Error!");
			e.printStackTrace();
		} catch (UndeclaredIdentifierException e) {
			e.printStackTrace();
		} catch (DuplicateIdentifierException e) {
			e.printStackTrace();
		}
	}

}