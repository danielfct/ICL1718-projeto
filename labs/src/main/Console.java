package main;

import java.io.ByteArrayInputStream;

import ast.ASTNode;
import parser.ParseException;
import parser.Parser;
import parser.SimpleParser;
import parser.TokenMgrError;
import util.Environment;
import values.IValue;
import values.IntValue;

public class Console {

	@SuppressWarnings("static-access")
	public static void main(String args[]) {
		SimpleParser parser = new SimpleParser(System.in);

		while (true) {
			try {
				parser.Start();
				System.out.println("OK!" );
			} catch (TokenMgrError e) {
				System.out.println("Lexical Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
			} catch (ParseException e) {
				System.out.println("Syntax Error!");
				e.printStackTrace();
				parser.ReInit(System.in);
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

	public static boolean acceptCompare(String s, int value) {
		Parser parser = new Parser(new ByteArrayInputStream(s.getBytes()));
		try {
			ASTNode n = parser.Start();
			return n.eval(new Environment<IValue>()) == value;
//			return n.eval() == new IntValue(value);
		} catch (TokenMgrError e) {
			return false;
		} catch (ParseException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}


}
