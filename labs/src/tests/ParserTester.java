package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import main.Console;
import parser.ParseException;


public class ParserTester {

	private void testCase(String expression) throws ParseException {
		assertTrue(Console.accept(expression));		
	}
	
	private void testNegativeCase(String expression) throws ParseException {
		assertFalse(Console.accept(expression));
	}
	
	@Test
	public void testAdd() throws Exception {
		testCase("1+2\n");
		testCase("1+2+3\n");
		testCase("(1+2)+4\n");
		testCase("1+(2+4)+5\n");
		testNegativeCase("1+\n");
		testNegativeCase("+1\n");
		testNegativeCase("1++2\n");
	}
	
	@Test
	public void testAnd() throws Exception {
		testCase("true && true\n");
		testCase("true && false\n");
		testCase("false && false\n");
		testCase("true && (true && false)\n");
		testNegativeCase("true &&\n");
		testNegativeCase("&& false\n");
		testNegativeCase("false &&& false\n");
	}
	
	@Test
	public void testBool() throws Exception {
		testCase("true\n");
		testCase("false\n");
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1\n");
		testCase("6/3/2\n");
		testCase("4/2/2\n");
		testNegativeCase("1/\n");
		testNegativeCase("/1\n");
		testNegativeCase("1//1\n");
		testNegativeCase("4/+2/2\n");
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1\n");
		testCase("1 == 2\n");
		testCase("true == true\n");
		testCase("true == false\n");
		testNegativeCase("1 ==\n");
		testNegativeCase("== 1\n");
		testNegativeCase("true ==\n");
		testNegativeCase("false ==\n");
		testNegativeCase("1 === 1\n");
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1\n");
		testCase("3 > 2 && 2 > 1\n");
		testCase("3 > 1 && 1 > 2\n");
		testNegativeCase("10 > \n");
		testNegativeCase("> 10\n");
		testNegativeCase("1 >> 10\n");
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1\n");
		testCase("2 >= 1\n");
		testCase("1 >= 2\n");
		testNegativeCase("1 >= \n");
		testNegativeCase(">= 2\n");
		testNegativeCase("1 >>= 2\n");
		testNegativeCase("1 >== 2\n");
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2\n");
		testCase("1 < 2 && 2 < 3\n");
		testCase("2 < 1\n");
		testCase("1 < 2 && 2 < 1\n");
		testNegativeCase("1 <\n");
		testNegativeCase("< 2\n");
		testNegativeCase("1 << 2\n");
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1\n");
		testCase("1 <= 2\n");
		testCase("2 <= 1\n");
		testNegativeCase("1 <=\n");
		testNegativeCase("<= 1\n");
		testNegativeCase("1 <<= 1\n");
		testNegativeCase("1 <== 1\n");
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2\n");
		testCase("4*2*2\n");
		testCase("-1*3\n");
		testNegativeCase("1*\n");
		testNegativeCase("*1\n");
		testNegativeCase("1**1\n");
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false\n");
		testCase("!true\n");
		testCase("!(false)\n");
		testNegativeCase("not false\n");
		testNegativeCase("!true!\n");
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2\n");
		testCase("1 != 1\n");
		testCase("true != false\n");
		testCase("true != true\n");
		testNegativeCase("1 !=\n");
		testNegativeCase("!= 1\n");
		testNegativeCase("true !=\n");
		testNegativeCase("!= true\n");
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1\n");
		testCase("10\n");
		testCase("-1\n");
	}
	
	@Test
	public void testOr() throws Exception {
		testCase("true || true\n");
		testCase("true || false\n");
		testCase("false || true\n");
		testCase("false || false\n");
		testNegativeCase("true ||\n");
		testNegativeCase("|| false\n");
		testNegativeCase("false ||| true\n");
	}
	
	@Test
	public void testSub() throws Exception {
		testCase("1-1\n");
		testCase("1-2-3\n");
		testCase("--1\n"); // == 1, bug ou feature?
		testNegativeCase("1-\n");
		testNegativeCase("1-1-\n");
	}
	
	@Test
	public void testDecl() throws Exception {
		testCase("decl x = 1 in x+1 end\n");
		testCase("decl x = 1 in decl y = 2 in x+y end end\n");
		testCase("decl x = decl y = 2 in 2*y end in x*3 end\n");
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n");
		testCase("decl x = true in x && true end\n");
		testCase("decl x = false in x && true end\n");
		testCase("decl x = decl y = false in y || true end in x && true end\n");
		testCase("decl x = false in !x end\n");
		testNegativeCase("decl x = false in !x\n");
		testNegativeCase("decl x = decl y = 2 in 2*y in x*3 end\n");
		testNegativeCase("decl x = 1 in decl y = 2 in x+y end end end\n");
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("3*5 != 1+2 == true\n");
		testCase("1 == 2 && 3 == 4\n");
		testCase("1+2*3 > 1/1*1\n");
		testCase("1+2*3+2/2\n");
		testCase("true || false != false && false\n");
		testCase("(20+20)/(4*5)\n");
		testCase("3*5 != 1+2 == true\n");
		testCase("1 == 2 || 3 == 4 && xpto\n");
		testCase("!(1 == 2) && xpto\n");
		testCase("1 == 2 || 3 == 4 && xpto\n");
		testCase("!(1 == 2) && xpto\n");
		testNegativeCase("(20+20)(4*5)\n");
		testNegativeCase("true || false != false &&& false\n");	
	}
	
}