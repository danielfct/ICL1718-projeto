package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import main.Console;
import parser.ParseException;
import values.IValue;
import values.IntValue;

public class TypeCheckingTests {

	private void testCase(String expression, IValue value) throws ParseException {
		assertTrue(Console.acceptCompareValues(expression,value));		
	}
	
	private void testNegativeCase(String expression, IValue value) throws ParseException {
		assertFalse(Console.acceptCompareValues(expression,value));
	}
	
	// private void testCaseBool(String expression, boolean value) throws ParseException {
	// 	assertTrue(Console.acceptCompareBool(expression, new BoolValue(value)));		
	// }
	
	// private void testNegativeCaseBool(String expression, boolean value) throws ParseException {
	// 	assertFalse(Console.acceptCompareBool(expression, new BoolValue(value)));
	// }
	
	@Test
	public void test01() throws Exception {
		testCase("1\n",new IntValue(1));
		testCase("1+2\n",new IntValue(3));
		testCase("1-2-3\n",new IntValue(-4));
	}
	
	@Test
	public void testsLabClass02() throws Exception {
		testCase("4*2\n",new IntValue(8));
		testCase("4/2/2\n",new IntValue(1));
		testCase("-1\n",new IntValue(-1));
		// more tests for boolean values
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
}