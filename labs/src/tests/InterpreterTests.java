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
	
	@Test
	public void testAdd() throws Exception {
		testCase("1+2\n", IntType.singleton);
		testCase("1+2+3\n", IntType.singleton);
		testCase("(1+2)+4\n", IntType.singleton);
		testCase("1+(2+4)+5\n", IntType.singleton);
		testNegativeCase("1+1\n", BoolType.singleton);
		testNegativeCase("1+2\n", BoolType.singleton);
		testNegativeCase("(1+2)+1\n", BoolType.singleton);
	}
	
	@Test
	public void testAnd() throws Exception {
		testCase("true && true\n", BoolType.singleton);
		testCase("true && false\n", BoolType.singleton);
		testCase("false && false\n", BoolType.singleton);
		testCase("true && (true && false)\n", BoolType.singleton);
		testNegativeCase("true && true\n", IntType.singleton);
		testNegativeCase("true && false\n", IntType.singleton);
		testNegativeCase("false && false\n", IntType.singleton);
		testNegativeCase("true && (true && false)\n", IntType.singleton);
	}
	
	@Test
	public void testBool() throws Exception {
		testCase("true\n", BoolType.singleton);
		testCase("false\n", BoolType.singleton);
		testNegativeCase("true\n", IntType.singleton);
		testNegativeCase("false\n", IntType.singleton);
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1\n", IntType.singleton);
		testCase("6/3/2\n", IntType.singleton);
		testCase("4/2/2\n", IntType.singleton);
		testNegativeCase("1/1\n", BoolType.singleton);
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1\n", BoolType.singleton);
		testCase("1 == 2\n", BoolType.singleton);
		testCase("true == true\n", BoolType.singleton);
		testCase("true == false\n", BoolType.singleton);
		testNegativeCase("1 == 1\n", IntType.singleton);
		testNegativeCase("true == true\n", IntType.singleton);
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1\n", BoolType.singleton);
		testCase("3 > 2 && 2 > 1\n", BoolType.singleton);
		testCase("3 > 1 && 1 > 2\n", BoolType.singleton);
		testNegativeCase("10 > 1\n", IntType.singleton);
		testNegativeCase("1 > 10\n", IntType.singleton);
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1\n", BoolType.singleton);
		testCase("2 >= 1\n", BoolType.singleton);
		testCase("1 >= 2\n", BoolType.singleton);
		testNegativeCase("1 >= 1\n", IntType.singleton);
		testNegativeCase("1 >= 2\n", IntType.singleton);
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2\n", BoolType.singleton);
		testCase("1 < 2 && 2 < 3\n", BoolType.singleton);
		testCase("2 < 1\n", BoolType.singleton);
		testCase("1 < 2 && 2 < 1\n", BoolType.singleton);
		testNegativeCase("1 < 2\n", IntType.singleton);
		testNegativeCase("1 < 2 && 2 < 1\n", IntType.singleton);
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1\n", BoolType.singleton);
		testCase("1 <= 2\n", BoolType.singleton);
		testCase("2 <= 1\n", BoolType.singleton);
		testNegativeCase("1 <= 1\n", IntType.singleton);
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2\n", IntType.singleton);
		testCase("4*2*2\n", IntType.singleton);
		testNegativeCase("1*1\n", BoolType.singleton);
		testNegativeCase("1*1*0\n", BoolType.singleton);
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false\n", BoolType.singleton);
		testCase("!true\n", BoolType.singleton);
		testCase("!(false)\n", BoolType.singleton);
		testNegativeCase("!false\n", IntType.singleton);
		testNegativeCase("!true\n", IntType.singleton);
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2\n", BoolType.singleton);
		testCase("1 != 1\n", BoolType.singleton);
		testCase("true != false\n", BoolType.singleton);
		testCase("true != true\n", BoolType.singleton);
		testNegativeCase("1 != 2\n", IntType.singleton);
		testNegativeCase("true != true\n", IntType.singleton);
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1\n", IntType.singleton);
		testCase("10\n", IntType.singleton);
		testCase("-1\n", IntType.singleton);
		testNegativeCase("1\n", BoolType.singleton);
	}
	
	@Test
	public void testOr() throws Exception {
		testCase("true || true\n", BoolType.singleton);
		testCase("true || false\n", BoolType.singleton);
		testCase("false || true\n", BoolType.singleton);
		testCase("false || false\n", BoolType.singleton);
		testNegativeCase("true || true\n", IntType.singleton);
		testNegativeCase("true || false\n", IntType.singleton);
		testNegativeCase("false || true\n", IntType.singleton);
		testNegativeCase("false || false\n", IntType.singleton);
	}
	
	@Test
	public void testSub() throws Exception {
		testCase("1-1\n", IntType.singleton);
		testCase("1-2-3\n", IntType.singleton);
		testNegativeCase("1-1\n", BoolType.singleton);
		testNegativeCase("1-1-1\n", BoolType.singleton);
	}
	
	@Test
	public void testDecl() throws Exception {
		System.out.println("decl");
		testCase("decl x = 1 in x+1 end\n", IntType.singleton);
		testCase("decl x = 1 in decl y = 2 in x+y end end\n", IntType.singleton);
		testCase("decl x = decl y = 2 in 2*y end in x*3 end\n", IntType.singleton);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n", IntType.singleton);
		testCase("decl x = true in x && true end\n", BoolType.singleton);
		testCase("decl x = false in x && true end\n", BoolType.singleton);
		testCase("decl x = false in x && true end\n", BoolType.singleton);
		testCase("decl x = false in !x end\n", BoolType.singleton);
		testNegativeCase("decl x = 1 in x+1 end\n", BoolType.singleton);
		testNegativeCase("decl x = false in !x end\n", IntType.singleton);
		testNegativeCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end\n", IntType.singleton);
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("1+2*3 > 1/1*1\n", BoolType.singleton);
		testCase("1+2*3+2/2\n", IntType.singleton);
		testCase("true || false != false && false\n", BoolType.singleton);
		testCase("(20+20)/(4*5)\n", IntType.singleton);
		testNegativeCase("(20+20)/(4*5)\n", BoolType.singleton);
		testNegativeCase("true || false != false && false\n", IntType.singleton);
	}
	
}