package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Console;
import memory.MemoryCell;
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
	public void testAdd() throws Exception {
		testCase("1+2;;", new IntValue(3));
		testCase("1+2+3;;", new IntValue(6));
		testCase("(1+2)+4;;", new IntValue(7));
		testCase("1+(2+4)+5;;", new IntValue(12));
		testNegativeCase("1+1;;", new IntValue(0));
		testNegativeCase("1+2;;", new IntValue(1));
		testNegativeCase("(1+2)+1;;", new IntValue(2));
	}
	
	@Test
	public void testAnd() throws Exception {
		testCase("true && true;;", new BoolValue(true));
		testCase("true && false;;", new BoolValue(false));
		testCase("false && false;;", new BoolValue(false));
		testCase("true && (true && false);;", new BoolValue(false));
		testNegativeCase("true && true;;", new BoolValue(false));
		testNegativeCase("true && false;;", new BoolValue(true));
		testNegativeCase("false && false;;", new BoolValue(true));
		testNegativeCase("true && (true && false);;", new BoolValue(true));
	}
	
	@Test
	public void testBool() throws Exception {
		testCase("true;;", new BoolValue(true));
		testCase("false;;", new BoolValue(false));
		testNegativeCase("true;;", new BoolValue(false));
		testNegativeCase("false;;", new BoolValue(true));
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1;;", new IntValue(1));
		testCase("6/3/2;;", new IntValue(1));
		testCase("4/2/2;;", new IntValue(1));
		testNegativeCase("1/1;;", new IntValue(0));
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1;;", new BoolValue(true));
		testCase("1 == 2;;", new BoolValue(false));
		testCase("true == true;;", new BoolValue(true));
		testCase("true == false;;", new BoolValue(false));
		testNegativeCase("1 == 1;;", new BoolValue(false));
		testNegativeCase("true == true;;", new BoolValue(false));
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1;;", new BoolValue(true));
		testCase("3 > 2 && 2 > 1;;", new BoolValue(true));
		testCase("3 > 1 && 1 > 2;;", new BoolValue(false));
		testNegativeCase("10 > 1;;", new BoolValue(false));
		testNegativeCase("1 > 10;;", new BoolValue(true));
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1;;", new BoolValue(true));
		testCase("2 >= 1;;", new BoolValue(true));
		testCase("1 >= 2;;", new BoolValue(false));
		testNegativeCase("1 >= 1;;", new BoolValue(false));
		testNegativeCase("1 >= 2;;", new BoolValue(true));
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2;;", new BoolValue(true));
		testCase("1 < 2 && 2 < 3;;", new BoolValue(true));
		testCase("2 < 1;;", new BoolValue(false));
		testCase("1 < 2 && 2 < 1;;", new BoolValue(false));
		testNegativeCase("1 < 2;;", new BoolValue(false));
		testNegativeCase("1 < 2 && 2 < 1;;", new BoolValue(true));
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1;;", new BoolValue(true));
		testCase("1 <= 2;;", new BoolValue(true));
		testCase("2 <= 1;;", new BoolValue(false));
		testNegativeCase("1 <= 1;;", new BoolValue(false));
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2;;", new IntValue(8));
		testCase("4*2*2;;", new IntValue(16));
		testNegativeCase("1*1;;", new IntValue(0));
		testNegativeCase("1*1*0;;", new IntValue(1));
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false;;", new BoolValue(true));
		testCase("!true;;", new BoolValue(false));
		testCase("!(false);;", new BoolValue(true));
		testNegativeCase("!false;;", new BoolValue(false));
		testNegativeCase("!true;;", new BoolValue(true));
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2;;", new BoolValue(true));
		testCase("1 != 1;;", new BoolValue(false));
		testCase("true != false;;", new BoolValue(true));
		testCase("true != true;;", new BoolValue(false));
		testNegativeCase("1 != 2;;", new BoolValue(false));
		testNegativeCase("true != true;;", new BoolValue(true));
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1;;", new IntValue(1));
		testCase("10;;", new IntValue(10));
		testCase("-1;;", new IntValue(-1));
		testNegativeCase("1;;", new IntValue(0));
	}
	
	@Test
	public void testOr() throws Exception {
		testCase("true || true;;", new BoolValue(true));
		testCase("true || false;;", new BoolValue(true));
		testCase("false || true;;", new BoolValue(true));
		testCase("false || false;;", new BoolValue(false));
		testNegativeCase("true || true;;", new BoolValue(false));
		testNegativeCase("true || false;;", new BoolValue(false));
		testNegativeCase("false || true;;", new BoolValue(false));
		testNegativeCase("false || false;;", new BoolValue(true));
	}
	
	@Test
	public void testSub() throws Exception {
		testCase("1-1;;", new IntValue(0));
		testCase("1-2-3;;", new IntValue(-4));
		testNegativeCase("1-1;;", new IntValue(1));
		testNegativeCase("1-1-1;;", new IntValue(0));
	}
	
	@Test
	public void testDecl() throws Exception {
		testCase("decl x = 1 in x+1 end;;", new IntValue(2));
		testCase("decl x = 1 in decl y = 2 in x+y end end;;", new IntValue(3));
		testCase("decl x = decl y = 2 in 2*y end in x*3 end;;", new IntValue(12));
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end;;", new IntValue(6));
		testCase("decl x = true in x && true end;;", new BoolValue(true));
		testCase("decl x = false in x && true end;;", new BoolValue(false));
		testCase("decl x = decl y = false in y || true end in x && true end;;", new BoolValue(true));
		testCase("decl x = false in !x end;;", new BoolValue(true));
		testNegativeCase("decl x = 1 in x+1 end;;", new IntValue(0));
		testNegativeCase("decl x = false in !x end;;", new BoolValue(false));
		testNegativeCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end;;", new IntValue(0));
	}
	
	@Test
	public void testAssign() throws Exception {
		testCase("var(0) := 1;;", new IntValue(1));
		testCase("var(1) := 0;;", new IntValue(0));
		testCase("var(var(1)) := var(2);;", new MemoryCell(new IntValue(2)));
		testCase("var(var(true)) := var(false);;", new MemoryCell(new BoolValue(false)));
		testCase("var(0) := var(0) := 1;;", new IntValue(1));
		testNegativeCase("var(0) := 1;;", new IntValue(2));
		testNegativeCase("var(true) := false", new IntValue(0));
	}
	
	@Test
	public void testDeref() throws Exception {
		testCase("*var(0);;", new IntValue(0));
		testCase("*var(99);;", new IntValue(99));
		testCase("*var(true);;", new BoolValue(true));
		testCase("*var(*var(1));;", new IntValue(1));
		testCase("*var(var(1));;", new MemoryCell(new IntValue(1)));
		testCase("*var(var(true));;", new MemoryCell(new BoolValue(true)));
		testNegativeCase("*var(0);;", new BoolValue(false));
		testNegativeCase("*var(true);;", new IntValue(1));
	}
	
	@Test
	public void testVar() throws Exception {
		testCase("var(0);;", new MemoryCell(new IntValue(0)));
		testCase("var(true);;", new MemoryCell(new BoolValue(true)));
		testNegativeCase("var(false);;", new BoolValue(true));
		testNegativeCase("var(1);;", new IntValue(1));
	}
	
	@Test
	public void testWhile() throws Exception {
		testCase("decl x = var(0) in while *x < 1 do x := *x + 1 end end;;", new BoolValue(false)); // by default, while statement always returns false
		testCase("decl i = var(1) in " 
				+"		decl x = while *i > 0 do "
				+"      			i := *i - 1 "
				+"           	 end "
				+"		in "
				+"			x "
				+"		end "
				+"end;;", new BoolValue(false));
		testNegativeCase("decl x = var(0) in while *x < 1 do x := *x + 1 end end;;", new BoolValue(true));
		testNegativeCase("decl x = var(0) in while *x < 1 do x := *x + 1 end end;;", new IntValue(0));
	}
	
	@Test
	public void testSeq() throws Exception {
		testCase("1 ; 2 ; 3;;", new IntValue(3));
		testCase("true ; true ; false;;", new BoolValue(false));
		testCase("1 ; 2 ; true;;", new BoolValue(true));
		testCase("decl x = 1; 2 in x end;;", new IntValue(2));
		testNegativeCase("1 ; 2 ; 3;;", new IntValue(1));
		testNegativeCase("1 ; 2 ; true;;", new BoolValue(false));
	}
	
	@Test
	public void testIfThenElse() throws Exception {
		testCase("if true then 1 else 0 end;;", new IntValue(1));
		testCase("if false then 1 else 0 end;;", new IntValue(0));
		testCase("if true then false else true end;;", new BoolValue(false));
		testCase("if false then false else true end;;", new BoolValue(true));
		testNegativeCase("if true then false else true end;;", new BoolValue(true));
		testNegativeCase("if false then false else true end;;", new BoolValue(false));
		testNegativeCase("if true then false else true end;;", new IntValue(1));
		testNegativeCase("if false then false else true end;;", new IntValue(2));
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("1+2*3 > 1/1*1;;", new BoolValue(true));
		testCase("1+2*3+2/2;;", new IntValue(8));
		testCase("true || false != false && false;;", new BoolValue(true));
		testCase("(20+20)/(4*5);;", new IntValue(2));
		testCase("3*5 != 1+2 == true;;", new BoolValue(true));
        testCase("decl x = var(0) in x := 1; *x end;;", new IntValue(1));
        testCase("decl x = var(0) in decl y = x in x := 1; *y end end;;", new IntValue(1));
        testCase("decl x = var(0) in decl y = var(0) in x := 1; *y end end;;", new IntValue(0));
        testCase("decl x = var(0) in decl y = var(0) in while *x < 10 do y := *y + 2; x := *x + 1 end; *y end end;;", new IntValue(20));
        testCase("if true then 1 else 2 end;;", new IntValue(1));
        testCase("if false then 1 else 2 end;;", new IntValue(2));
        testCase("decl x = var(3) in "
                + "decl y = var(1) in "
                + "while *x > 0 do "
                + "  y := *y * *x; "
                + "  x := *x - 1 "
                + "end; "
                + "*y "
                + "end "
                + "end;;", new IntValue(6));
        testCase("decl x = var(3) in\n"
                + "decl y = var(1) in\n"
                + "while *x > 0 do\n"
                + "  y := *y * *x;\n"
                + "  x := *x - 1\n"
                + "end;\n"
                + "*y\n"
                + "end\n"
                + "end;;", new IntValue(6));
        testCase("decl x = 2;var(0;1) in (2;x) := *x+1; *x end;;", new IntValue(2));
        testCase("***var(var(var(1)));;", new IntValue(1));
		testNegativeCase("(20+20)/(4*5);;", new IntValue(1));
		testNegativeCase("true || false != false && false;;", new BoolValue(false));
		testNegativeCase("1 == 2 || 3 == 4 && xpto;;", new BoolValue(true));
		testNegativeCase("!(1 == 2) && xpto;;", new BoolValue(true));
	}
	
}