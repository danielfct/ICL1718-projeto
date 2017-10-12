package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import main.Console;
import parser.ParseException;

public class InterpreterTests {

	private void testCase(String expression, int value) throws ParseException {
		assertTrue(Console.acceptCompare(expression,value));		
	}
	
	private void testNegativeCase(String expression, int value) throws ParseException {
		assertFalse(Console.acceptCompare(expression, value));
	}
	
	// private void testCaseBool(String expression, boolean value) throws ParseException {
	// 	assertTrue(Console.acceptCompareBool(expression, new BoolValue(value)));		
	// }
	
	// private void testNegativeCaseBool(String expression, boolean value) throws ParseException {
	// 	assertFalse(Console.acceptCompareBool(expression, new BoolValue(value)));
	// }
	
	@Test
	public void test01() throws Exception {
		testCase("1\n",1);
		testCase("1+2\n",3);
		testCase("1-2-3\n",-4);
	}
	
	@Test
	public void testsLabClass02() throws Exception {
		testCase("4*2\n",8);
		testCase("4/2/2\n",1);
		testCase("-1\n", -1);
		testCase("-1*3\n",-3);
		// testCaseBool("true\n",true);
		// testCaseBool("false\n",false);
		// testCaseBool("11 < 22\n", true);
		// testCaseBool("11 > 22\n", false);
		// testCaseBool("11 == 22\n", false);
		// testCaseBool("3*5 != 1+2 == true\n", true);
		// testCaseBool("1 == 2 && 3 == 4\n", false);
		// testCaseNegativeBool("1 == 2 || 3 == 4 && xpto \n", true);
		// testCaseNegativeBool("!(1 == 2) && xpto \n", true);
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
