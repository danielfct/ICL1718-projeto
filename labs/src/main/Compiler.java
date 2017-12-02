package main;

import compiler.*;
import environment.CompilationEnvironment;
import environment.DuplicateIdentifierException;
import environment.Environment;
import environment.UndeclaredIdentifierException;
import parser.ParseException;
import parser.Parser;
import parser.TokenMgrError;
import types.IType;
import types.TypingException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import ast.ASTNode;

public class Compiler {

	public static final String DIR = System.getProperty("user.dir");
	public static final String MAIN_FILE = "Main.j";

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
			IType t = n.typecheck(new Environment<IType>());
			CodeBlock code = new CodeBlock();
			n.compile(code, new CompilationEnvironment());
			PrintStream out = new PrintStream(new FileOutputStream(DIR + "/" + MAIN_FILE));
			code.dump(out, t.toJasmin());
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
			System.out.println("Undeclared Identifier!");
			e.printStackTrace();
		} catch (DuplicateIdentifierException e) {
			System.out.println("Duplicate Identifier!");
			e.printStackTrace();
		}
	}

}