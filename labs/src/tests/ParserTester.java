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
		testCase("1+2;;");
		testCase("1+2+3;;");
		testCase("(1+2)+4;;");
		testCase("1+(2+4)+5;;");
		testNegativeCase("1+;;");
		testNegativeCase("+1;;");
		testNegativeCase("1++2;;");
	}
	
	@Test
	public void testAnd() throws Exception {
		testCase("true && true;;");
		testCase("true && false;;");
		testCase("false && false;;");
		testCase("true && (true && false);;");
		testNegativeCase("true &&;;");
		testNegativeCase("&& false;;");
		testNegativeCase("false &&& false;;");
	}
	
	@Test
	public void testBool() throws Exception {
		testCase("true;;");
		testCase("false;;");
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1;;");
		testCase("6/3/2;;");
		testCase("4/2/2;;");
		testNegativeCase("1/;;");
		testNegativeCase("/1;;");
		testNegativeCase("1//1;;");
		testNegativeCase("4/+2/2;;");
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1;;");
		testCase("1 == 2;;");
		testCase("true == true;;");
		testCase("true == false;;");
		testNegativeCase("1 ==;;");
		testNegativeCase("== 1;;");
		testNegativeCase("true ==;;");
		testNegativeCase("false ==;;");
		testNegativeCase("1 === 1;;");
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1;;");
		testCase("3 > 2 && 2 > 1;;");
		testCase("3 > 1 && 1 > 2;;");
		testNegativeCase("10 > ;;");
		testNegativeCase("> 10;;");
		testNegativeCase("1 >> 10;;");
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1;;");
		testCase("2 >= 1;;");
		testCase("1 >= 2;;");
		testNegativeCase("1 >= ;;");
		testNegativeCase(">= 2;;");
		testNegativeCase("1 >>= 2;;");
		testNegativeCase("1 >== 2;;");
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2;;");
		testCase("1 < 2 && 2 < 3;;");
		testCase("2 < 1;;");
		testCase("1 < 2 && 2 < 1;;");
		testNegativeCase("1 <;;");
		testNegativeCase("< 2;;");
		testNegativeCase("1 << 2;;");
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1;;");
		testCase("1 <= 2;;");
		testCase("2 <= 1;;");
		testNegativeCase("1 <=;;");
		testNegativeCase("<= 1;;");
		testNegativeCase("1 <<= 1;;");
		testNegativeCase("1 <== 1;;");
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2;;");
		testCase("4*2*2;;");
		testCase("-1*3;;");
		testNegativeCase("1*;;");
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false;;");
		testCase("!true;;");
		testCase("!(false);;");
		testNegativeCase("not false;;");
		testNegativeCase("!true!;;");
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2;;");
		testCase("1 != 1;;");
		testCase("true != false;;");
		testCase("true != true;;");
		testNegativeCase("1 !=;;");
		testNegativeCase("!= 1;;");
		testNegativeCase("true !=;;");
		testNegativeCase("!= true;;");
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1;;");
		testCase("10;;");
		testCase("-1;;");
	}
	
	@Test
	public void testOr() throws Exception {
		testCase("true || true;;");
		testCase("true || false;;");
		testCase("false || true;;");
		testCase("false || false;;");
		testNegativeCase("true ||;;");
		testNegativeCase("|| false;;");
		testNegativeCase("false ||| true;;");
	}
	
	@Test
	public void testSub() throws Exception {
		testCase("1-1;;");
		testCase("1-2-3;;");
		testCase("--1;;");
		testNegativeCase("1-;;");
		testNegativeCase("1-1-;;");
	}
	
	@Test
	public void testDecl() throws Exception {
		testCase("decl x = 1 in x+1 end;;");
		testCase("decl x = 1 in decl y = 2 in x+y end end;;");
		testCase("decl x = decl y = 2 in 2*y end in x*3 end;;");
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end;;");
		testCase("decl x = true in x && true end;;");
		testCase("decl x = false in x && true end;;");
		testCase("decl x = decl y = false in y || true end in x && true end;;");
		testCase("decl x = false in !x end;;");
		testNegativeCase("decl x = false in !x;;");
		testNegativeCase("decl x = decl y = 2 in 2*y in x*3 end;;");
		testNegativeCase("decl x = 1 in decl y = 2 in x+y end end end;;");
	}
	
	@Test
	public void testAssign() throws Exception {
		testCase("var(0) := 1;;");
		testCase("var(1) := 0;;");
		testCase("var(0) := true;;");
		testNegativeCase("var(0) = 1;;");
	}
	
	@Test
	public void testDeref() throws Exception {
		testCase("*var(0);;");
		testCase("*var(99);;");
		testCase("*var(true);;");
	}
	
	@Test
	public void testVar() throws Exception {
		testCase("var(0);;");
		testCase("var(true);;");
		testNegativeCase("var();;");
	}
	
	@Test
	public void testWhile() throws Exception {
		testCase("while true do 1 end;;");
		testCase("while false do 1 end;;");
		testNegativeCase("while do 1 end;;");
		testNegativeCase("while true do end;;");
		testNegativeCase("while true do 1;;");
	}
	
	@Test
	public void testSeq() throws Exception {
		testCase("1 ; 2 ; 3;;");
		testCase("true ; true ; false;;");
		testCase("decl x = 1; 2 in x end;;");
	}
	
	@Test
	public void testIfThenElse() throws Exception {
		testCase("if true then 1 else 2 end;;");
		testCase("if false then 1 else 2 end;;");
		testNegativeCase("if true then 1 else 2;;");
		testNegativeCase("if true than 1 else 2;;");
		testNegativeCase("if then 1 else 2;;");
		testNegativeCase("if true then else 2;;");
		testNegativeCase("if true then 1 else;;");
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("3*5 != 1+2 == true;;");
		testCase("1 == 2 && 3 == 4;;");
		testCase("1+2*3 > 1/1*1;;");
		testCase("1+2*3+2/2;;");
		testCase("true || false != false && false;;");
		testCase("(20+20)/(4*5);;");
		testCase("3*5 != 1+2 == true;;");
		testCase("1 == 2 || 3 == 4 && xpto;;");
		testCase("!(1 == 2) && xpto;;");
		testCase("1 == 2 || 3 == 4 && xpto;;");
		testCase("!(1 == 2) && xpto;;");
		testCase("decl x = var(0) in x := 1; *x end;;");
		testCase("decl x = var(0) in decl y = x in x := 1; *y end end;;");
		testCase("decl x = var(0) in decl y = var(0) in x := 1; *y end end;;");
		testCase("decl x = var(0) in decl y = var(0) in while *x < 10 do y := *y + 2; x := *x + 1 end end end;;");
		testCase("decl x = var(3) in "
				+ "decl y = var(1) in "
				+ "while *x > 0 do "
				+ "  y := *y * *x; "
				+ "  x := *x - 1 "
				+ "end "
				+ "end "
				+ "end;;");
		testCase("decl x = var(3) in\n"
                + "decl y = var(1) in\n"
                + "while *x > 0 do\n"
                + "  y := *y * *x;\n"
                + "  x := *x - 1\n"
                + "end;\n"
                + "*y\n"
                + "end\n"
                + "end;;");
        testCase("decl x = 2;var(0;1) in (2;x) := *x+1; *x end;;");
        testCase("***var(var(var(1)));;");
		testNegativeCase("(20+20)(4*5);;");
		testNegativeCase("true || false != false &&& false;;");	
	}
	
}