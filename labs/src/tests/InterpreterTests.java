package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Console;
import parser.ParseException;
import values.BoolValue;
import values.IValue;
import values.IntValue;

public class InterpreterTests {

	private void testCase(String expression, IValue value) throws ParseException {
		assertTrue(Console.acceptCompareValues(expression, value));		
	}
	
	private void testNegativeCase(String expression, IValue value) throws ParseException {
		assertFalse(Console.acceptCompareValues(expression, value));
	}
	
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
	
	@Test
	public void testAdd() throws Exception {
		testCase("1+2\n", new IntValue(3));
		testCase("1+2+3\n", new IntValue(6));
		testCase("(1+2)+4\n", new IntValue(7));
		testCase("1+(2+4)+5\n", new IntValue(12));
		testNegativeCase("1+1\n", new IntValue(0));
		testNegativeCase("1+2\n", new IntValue(1));
		testNegativeCase("(1+2)+1\n", new IntValue(2));
	}
	
	@Test
	public void testAnd() throws Exception {
		testCase("true && true\n", new BoolValue(true));
		testCase("true && false\n", new BoolValue(false));
		testCase("false && false\n", new BoolValue(false));
		testCase("true && (true && false)\n", new BoolValue(false));
		testNegativeCase("true && true\n", new BoolValue(false));
		testNegativeCase("true && false\n", new BoolValue(true));
		testNegativeCase("false && false\n", new BoolValue(true));
		testNegativeCase("true && (true && false)\n", new BoolValue(true));
	}
	
	@Test
	public void testBool() throws Exception {
		testCase("true\n", new BoolValue(true));
		testCase("false\n", new BoolValue(false));
		testNegativeCase("true\n", new BoolValue(false));
		testNegativeCase("false\n", new BoolValue(true));
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1\n", new IntValue(1));
		testCase("6/3/2\n", new IntValue(1));
		testCase("4/2/2\n", new IntValue(1));
		testNegativeCase("1/1\n", new IntValue(0));
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1\n", new BoolValue(true));
		testCase("1 == 2\n", new BoolValue(false));
		testCase("true == true\n", new BoolValue(true));
		testCase("true == false\n", new BoolValue(false));
		testNegativeCase("1 == 1\n", new BoolValue(false));
		testNegativeCase("true == true\n", new BoolValue(false));
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1\n", new BoolValue(true));
		testCase("3 > 2 && 2 > 1\n", new BoolValue(true));
		testCase("3 > 1 && 1 > 2\n", new BoolValue(false));
		testNegativeCase("10 > 1\n", new BoolValue(false));
		testNegativeCase("1 > 10\n", new BoolValue(true));
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1\n", new BoolValue(true));
		testCase("2 >= 1\n", new BoolValue(true));
		testCase("1 >= 2\n", new BoolValue(false));
		testNegativeCase("1 >= 1\n", new BoolValue(false));
		testNegativeCase("1 >= 2\n", new BoolValue(true));
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2\n", new BoolValue(true));
		testCase("1 < 2 && 2 < 3\n", new BoolValue(true));
		testCase("2 < 1\n", new BoolValue(false));
		testCase("1 < 2 && 2 < 1\n", new BoolValue(false));
		testNegativeCase("1 < 2\n", new BoolValue(false));
		testNegativeCase("1 < 2 && 2 < 1\n", new BoolValue(true));
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1\n", new BoolValue(true));
		testCase("1 <= 2\n", new BoolValue(true));
		testCase("2 <= 1\n", new BoolValue(false));
		testNegativeCase("1 <= 1\n", new BoolValue(false));
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2\n", new IntValue(8));
		testCase("4*2*2\n", new IntValue(16));
		testNegativeCase("1*1\n", new IntValue(0));
		testNegativeCase("1*1*0\n", new IntValue(1));
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false\n", new BoolValue(true));
		testCase("!true\n", new BoolValue(false));
		testCase("!(false)\n", new BoolValue(true));
		testNegativeCase("!false\n", new BoolValue(false));
		testNegativeCase("!true\n", new BoolValue(true));
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2\n", new BoolValue(true));
		testCase("1 != 1\n", new BoolValue(false));
		testCase("true != false\n", new BoolValue(true));
		testCase("true != true\n", new BoolValue(false));
		testNegativeCase("1 != 2\n", new BoolValue(false));
		testNegativeCase("true != true\n", new BoolValue(true));
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1\n", new IntValue(1));
		testCase("10\n", new IntValue(10));
		testCase("-1\n", new IntValue(-1));
		testNegativeCase("1\n", new IntValue(0));
	}
	
	@Test
	public void testOr() throws Exception {
		testCase("true || true\n", new BoolValue(true));
		testCase("true || false\n", new BoolValue(true));
		testCase("false || true\n", new BoolValue(true));
		testCase("false || false\n", new BoolValue(false));
		testNegativeCase("true || true\n", new BoolValue(false));
		testNegativeCase("true || false\n", new BoolValue(false));
		testNegativeCase("false || true\n", new BoolValue(false));
		testNegativeCase("false || false\n", new BoolValue(true));
	}
	
	@Test
	public void testSub() throws Exception {
		testCase("1-1\n", new IntValue(0));
		testCase("1-2-3\n", new IntValue(-4));
		testNegativeCase("1-1\n", new IntValue(1));
		testNegativeCase("1-1-1\n", new IntValue(0));
	}
	
	@Test
	public void testDecl() throws Exception {
		testCase("decl x = 1 in x+1 end\n", new IntValue(2));
		testCase("decl x = 1 in decl y = 2 in x+y end end\n", new IntValue(3));
		testCase("decl x = decl y = 2 in 2*y end in x*3 end\n", new IntValue(12));
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n", new IntValue(6));
		testCase("decl x = true in x && true end\n", new BoolValue(true));
		testCase("decl x = false in x && true end\n", new BoolValue(false));
		testCase("decl x = decl y = false in y || true end in x && true end\n", new BoolValue(true));
		testCase("decl x = false in !x end\n", new BoolValue(true));
		testNegativeCase("decl x = 1 in x+1 end\n", new IntValue(0));
		testNegativeCase("decl x = false in !x end\n", new BoolValue(false));
		testNegativeCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end\n", new IntValue(0));
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("1+2*3 > 1/1*1\n", new BoolValue(true));
		testCase("1+2*3+2/2\n", new IntValue(8));
		testCase("true || false != false && false\n", new BoolValue(true));
		testCase("(20+20)/(4*5)\n", new IntValue(2));
		testNegativeCase("(20+20)/(4*5)\n", new IntValue(1));
		testNegativeCase("true || false != false && false\n", new BoolValue(false));
	}
}