package main;

import java.io.ByteArrayInputStream;

import ast.ASTNode;
import parser.ParseException;
import parser.Parser;
import parser.SimpleParser;
import parser.TokenMgrError;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class Console {

	public static void main(String args[]) {
		Parser parser = new Parser(System.in);

		while (true) {
			try {
				ASTNode n = parser.Start();
				n.typecheck();
				System.out.println("OK! = " + n.eval());
			} catch (TokenMgrError e) {
				System.out.println("Lexical Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (ParseException e) {
				System.out.println("Syntax Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (TypingException e) {
				System.out.println("Static Typing Error!");
				e.printStackTrace();
			} catch (TypeMismatchException e) {
				System.out.println("Dynamic Typing Error!");
				e.printStackTrace();
			}
		}
	}

	public static boolean accept(String s) throws ParseException {
		SimpleParser parser = new SimpleParser(new ByteArrayInputStream(s.getBytes()));
		try {
			parser.Start();
			return true;
		} catch (TokenMgrError e) {
			return false;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean acceptCompare(String s, IValue value) {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		try {
			ASTNode n = parser.Start();
			return n.equals(value);
		} catch (TokenMgrError e) {
			return false;
		} catch (ParseException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean acceptCompareTypes(String s, IType type) {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		try {
			ASTNode n = parser.Start();
			return n.typecheck().equals(type);
		} catch (TokenMgrError e) {
			return false;
		} catch (ParseException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}


}
