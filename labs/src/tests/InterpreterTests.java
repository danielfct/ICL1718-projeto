package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import main.Console;
import parser.ParseException;
import types.BoolType;
import types.IType;
import types.IntType;
import values.IValue;
import values.IntValue;

public class InterpreterTests {

	private void testCase(String expression, IType type) throws ParseException {
		assertTrue(Console.acceptCompareTypes(expression,type));		
	}
	
	private void testNegativeCase(String expression, IType type) throws ParseException {
		assertFalse(Console.acceptCompareTypes(expression,type));
	}
		
	@Test
	public void test01() throws Exception {
		testCase("1\n", IntType.singleton);
		testCase("99\n", IntType.singleton);
		testCase("10+23\n", IntType.singleton);
		testCase("1-2-3\n", IntType.singleton);
	}
	
	@Test
	public void testsLabClass02() throws Exception {
		 testCase("true\n", BoolType.singleton);
		 testCase("false\n", BoolType.singleton);
		 testCase("11 < 22\n", BoolType.singleton);
		 testCase("11 > 22\n", BoolType.singleton);
	}
	
	@Test
	public void testsLabClass05() throws Exception {
		testCase("decl x = 1 in x+1 end\n", 2);
		testCase("decl x = 1 in decl y = 2 in x+y end end\n", 3);
		testCase("decl x = decl y = 2 in 2*y end in x*3\n", 4);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n", 5);
		testNegativeCase("x+1", 0);
		testNegativeCase("decl x = 1 in x+1 end + x", 0);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end\n", 5);		
	}
}