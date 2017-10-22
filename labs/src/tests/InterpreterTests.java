package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Console;
import parser.ParseException;
import types.BoolType;
import types.IType;
import types.IntType;

public class InterpreterTests {

	private void testCase(String expression, IType type) throws ParseException {
		assertTrue(Console.acceptCompareTypes(expression, type));		
	}

	private void testNegativeCase(String expression, IType type) throws ParseException {
		assertFalse(Console.acceptCompareTypes(expression, type));
	}

//	private void testCaseBool(String expression, IType type) throws ParseException {
//		assertTrue(Console.acceptCompareType(expression, new BoolValue(value)));		
//	}
//
//	private void testCaseNegativeBool(String expression, IType type) throws ParseException {
//		assertFalse(Console.acceptCompareType(expression, type));
//	}

	@Test
	public void test01() throws Exception {
		testCase("1\n", IntType.singleton);
		testCase("1+2\n", IntType.singleton);
		testCase("1-2-3\n", IntType.singleton);
	}

	@Test
	public void testsLabClass02() throws Exception {
		testCase("4*2\n", IntType.singleton);
		testCase("4/2/2\n", IntType.singleton);
		testCase("-1\n", IntType.singleton);
		testCase("-1*3\n", IntType.singleton);
		testCase("true\n", BoolType.singleton);
		testCase("false\n", BoolType.singleton);
		testNegativeCase("false\n", IntType.singleton);
//		testCaseBool("11 < 22\n", true);
//		testCaseBool("11 > 22\n", false);
//		testCaseBool("11 == 22\n", false);
//		testCaseBool("3*5 != 1+2 == true\n", true);
//		testCaseBool("1 == 2 && 3 == 4\n", false);
//		testCaseNegativeBool("1 == 2 || 3 == 4 && xpto \n", true);
//		testCaseNegativeBool("!(1 == 2) && xpto \n", true);
	}
	
	@Test
	public void testsLabClass05() throws Exception {
		testCase("decl x = 1 in x+1 end\n",2);
		testCase("decl x = 1 in decl y = 2 in x+y end end\n",3);
		testCase("decl x = decl y = 2 in 2*y end in x*3\n",4);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n", 5);
		testNegativeCase("x+1", 0);
		testNegativeCase("decl x = 1 in x+1 end + x", 0);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end\n", 5);		
	}
}
