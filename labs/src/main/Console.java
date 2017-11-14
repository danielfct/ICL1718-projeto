package main;

import java.io.ByteArrayInputStream;

import ast.ASTNode;
import environment.DuplicateIdentifierException;
import environment.Environment;
import environment.UndeclaredIdentifierException;
import parser.ParseException;
import parser.Parser;
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
				n.typecheck(new Environment<>()); 
				System.out.println("OK! " + n.eval(new Environment<>()));
			} catch (TokenMgrError e) {
				System.out.println("Lexical Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (ParseException e) {
				System.out.println("Syntax Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (TypingException e) {
				System.out.println("Typing Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (UndeclaredIdentifierException e) {
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (DuplicateIdentifierException e) {
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (TypeMismatchException e) {
				System.out.println("Type Mismatch Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			}
		}
	}

	public static boolean accept(String s) throws ParseException {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		try {
			parser.Start();
			return true;
		} catch (TokenMgrError | ParseException e) {
			return false;
		}
	}

	public static boolean acceptCompareValues(String s, IValue value) {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		try {
			ASTNode n = parser.Start();
			return n.eval(new Environment<>()).equals(value);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean acceptCompareTypes(String s, IType type) {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		try {
			ASTNode n = parser.Start();
			return n.typecheck(new Environment<>()).equals(type);
		} catch (Exception e) {
			return false;
		}
	}


}