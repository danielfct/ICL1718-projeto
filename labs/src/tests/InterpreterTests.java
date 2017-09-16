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
		assertFalse(Console.acceptCompare(expression,value));
	}
	
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
	}
}
