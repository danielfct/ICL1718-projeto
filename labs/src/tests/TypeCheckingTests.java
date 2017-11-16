package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Console;
import parser.ParseException;
import types.BoolType;
import types.IType;
import types.IntType;
import types.RefType;

public class TypeCheckingTests {

	private void testCase(String expression, IType type) throws ParseException {
		assertTrue(Console.acceptCompareTypes(expression, type));		
	}
	
	private void testNegativeCase(String expression, IType type) throws ParseException {
		assertFalse(Console.acceptCompareTypes(expression, type));
	}
	
	private void testExceptionCase(String expression) throws ParseException {
		testNegativeCase(expression, null);
	}
	
	@Test
	public void testAdd() throws Exception {
		testCase("1+2;;", IntType.singleton);
		testCase("1+2+3;;", IntType.singleton);
		testCase("(1+2)+4;;", IntType.singleton);
		testCase("1+(2+4)+5;;", IntType.singleton);
		testNegativeCase("1+1;;", BoolType.singleton);
		testNegativeCase("1+2;;", BoolType.singleton);
		testNegativeCase("(1+2)+1;;", BoolType.singleton);
		testExceptionCase("1+false;;");
		testExceptionCase("1+xpto;;");
	}
	
	@Test
	public void testAnd() throws Exception {
		testCase("true && true;;", BoolType.singleton);
		testCase("true && false;;", BoolType.singleton);
		testCase("false && false;;", BoolType.singleton);
		testCase("true && (true && false);;", BoolType.singleton);
		testNegativeCase("true && true;;", IntType.singleton);
		testNegativeCase("true && false;;", IntType.singleton);
		testNegativeCase("false && false;;", IntType.singleton);
		testNegativeCase("true && (true && false);;", IntType.singleton);
		testExceptionCase("1 && false;;");
		testExceptionCase("xpto && false;;");
	}
	
	@Test
	public void testBool() throws Exception {
		testCase("true;;", BoolType.singleton);
		testCase("false;;", BoolType.singleton);
		testNegativeCase("true;;", IntType.singleton);
		testNegativeCase("false;;", IntType.singleton);
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1;;", IntType.singleton);
		testCase("6/3/2;;", IntType.singleton);
		testCase("4/2/2;;", IntType.singleton);
		testNegativeCase("1/1;;", BoolType.singleton);
		testExceptionCase("1/true;;");
		testExceptionCase("1/xpto;;");
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1;;", BoolType.singleton);
		testCase("1 == 2;;", BoolType.singleton);
		testCase("true == true;;", BoolType.singleton);
		testCase("true == false;;", BoolType.singleton);
		testNegativeCase("1 == 1;;", IntType.singleton);
		testNegativeCase("true == true;;", IntType.singleton);
		testExceptionCase("1 == true;;");
		testExceptionCase("1 == xpto;;");
		testExceptionCase("true == xpto;;");
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1;;", BoolType.singleton);
		testCase("3 > 2 && 2 > 1;;", BoolType.singleton);
		testCase("3 > 1 && 1 > 2;;", BoolType.singleton);
		testNegativeCase("10 > 1;;", IntType.singleton);
		testNegativeCase("1 > 10;;", IntType.singleton);
		testExceptionCase("1 > true;;");
		testExceptionCase("false > 1;;");
		testExceptionCase("false > false;;");
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1;;", BoolType.singleton);
		testCase("2 >= 1;;", BoolType.singleton);
		testCase("1 >= 2;;", BoolType.singleton);
		testNegativeCase("1 >= 1;;", IntType.singleton);
		testNegativeCase("1 >= 2;;", IntType.singleton);
		testExceptionCase("1 >= true;;");
		testExceptionCase("false >= false;;");
		testExceptionCase("false >= 1;;");
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2;;", BoolType.singleton);
		testCase("1 < 2 && 2 < 3;;", BoolType.singleton);
		testCase("2 < 1;;", BoolType.singleton);
		testCase("1 < 2 && 2 < 1;;", BoolType.singleton);
		testNegativeCase("1 < 2;;", IntType.singleton);
		testNegativeCase("1 < 2 && 2 < 1;;", IntType.singleton);
		testExceptionCase("1 < true;;");
		testExceptionCase("true < 1;;");
		testExceptionCase("true < true;;");
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1;;", BoolType.singleton);
		testCase("1 <= 2;;", BoolType.singleton);
		testCase("2 <= 1;;", BoolType.singleton);
		testNegativeCase("1 <= 1;;", IntType.singleton);
		testExceptionCase("1 <= true;;");
		testExceptionCase("true <= 1;;");
		testExceptionCase("true <= true;;");
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2;;", IntType.singleton);
		testCase("4*2*2;;", IntType.singleton);
		testNegativeCase("1*1;;", BoolType.singleton);
		testNegativeCase("1*1*0;;", BoolType.singleton);
		testExceptionCase("1*true;;");
		testExceptionCase("true*1;;");
		testExceptionCase("true*true;;");
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false;;", BoolType.singleton);
		testCase("!true;;", BoolType.singleton);
		testCase("!(false);;", BoolType.singleton);
		testNegativeCase("!false;;", IntType.singleton);
		testNegativeCase("!true;;", IntType.singleton);
		testExceptionCase("!1;;");
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2;;", BoolType.singleton);
		testCase("1 != 1;;", BoolType.singleton);
		testCase("true != false;;", BoolType.singleton);
		testCase("true != true;;", BoolType.singleton);
		testNegativeCase("1 != 2;;", IntType.singleton);
		testNegativeCase("true != true;;", IntType.singleton);
		testExceptionCase("1 != true;;");
		testExceptionCase("true != 1;;");
		testExceptionCase("true != true;;");
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1;;", IntType.singleton);
		testCase("10;;", IntType.singleton);
		testCase("-1;;", IntType.singleton);
		testNegativeCase("1;;", BoolType.singleton);
	}
	
	@Test
	public void testOr() throws Exception {
		testCase("true || true;;", BoolType.singleton);
		testCase("true || false;;", BoolType.singleton);
		testCase("false || true;;", BoolType.singleton);
		testCase("false || false;;", BoolType.singleton);
		testNegativeCase("true || true;;", IntType.singleton);
		testNegativeCase("true || false;;", IntType.singleton);
		testNegativeCase("false || true;;", IntType.singleton);
		testNegativeCase("false || false;;", IntType.singleton);
		testExceptionCase("1 || true;;");
		testExceptionCase("true || 1;;");
		testExceptionCase("1 || 1;;");
	}
	
	@Test
	public void testSub() throws Exception {
		testCase("1-1;;", IntType.singleton);
		testCase("1-2-3;;", IntType.singleton);
		testNegativeCase("1-1;;", BoolType.singleton);
		testNegativeCase("1-1-1;;", BoolType.singleton);
		testExceptionCase("1-true;;");
		testExceptionCase("true-1;;");
		testExceptionCase("true-true;;");
	}
	
	@Test
	public void testDecl() throws Exception {
		testCase("decl x = 1 in x+1 end;;", IntType.singleton);
		testCase("decl x = 1 in decl y = 2 in x+y end end;;", IntType.singleton);
		testCase("decl x = decl y = 2 in 2*y end in x*3 end;;", IntType.singleton);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end;;", IntType.singleton);
		testCase("decl x = true in x && true end;;", BoolType.singleton);
		testCase("decl x = false in x && true end;;", BoolType.singleton);
		testCase("decl x = decl y = false in y || true end in x && true end;;", BoolType.singleton);
		testCase("decl x = false in !x end;;", BoolType.singleton);
		testNegativeCase("decl x = 1 in x+1 end;;", BoolType.singleton);
		testNegativeCase("decl x = false in !x end;;", IntType.singleton);
		testNegativeCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end;;", IntType.singleton);
		testExceptionCase("decl x = true in 1+x end;;");
		testExceptionCase("decl x = 1 in 1+true end;;");
	}
	
	@Test
	public void testAssign() throws Exception {
		testCase("var(0) := 1;;", IntType.singleton);
		testCase("var(1) := 0;;", IntType.singleton);
		testCase("var(var(1)) := var(2);;", new RefType(IntType.singleton));
		testCase("var(var(true)) := var(false);;", new RefType(BoolType.singleton));
		testNegativeCase("var(0) := 1;;", new RefType(BoolType.singleton));
		testNegativeCase("var(0) := 1;;", BoolType.singleton);
		testExceptionCase("x := 1;;");
		testExceptionCase("var(0) := true;;");
	}
	
	@Test
	public void testDeref() throws Exception {
		testCase("*var(0);;", IntType.singleton);
		testCase("*var(99);;", IntType.singleton);
		testCase("*var(true);;", BoolType.singleton);
		testCase("*var(*var(1));;", IntType.singleton);
		testCase("*var(var(1));;", new RefType(IntType.singleton));
		testCase("*var(var(true));;", new RefType(BoolType.singleton));
		testNegativeCase("*var(0);;", BoolType.singleton);
		testNegativeCase("*var(true);;", IntType.singleton);
		testExceptionCase("*x;;");
		testExceptionCase("decl x = 1 in *x end;;");
	}
	
	@Test
	public void testVar() throws Exception {
		testCase("var(0);;", new RefType(IntType.singleton));
		testCase("var(true);;", new RefType(BoolType.singleton));
		testNegativeCase("var(false);;", new RefType(IntType.singleton));
		testNegativeCase("var(1);;", IntType.singleton);
	}
	
	@Test
	public void testWhile() throws Exception {
		testCase("while true do 1 end;;", BoolType.singleton);
		testCase("while false do 1 end;;", BoolType.singleton);
		testNegativeCase("while true do 1 end;;", new RefType(BoolType.singleton));
		testNegativeCase("while true do 1 end;;", new RefType(IntType.singleton));
		testExceptionCase("while 1 do 1 end;;");
		testExceptionCase("while 1 do 1 end;;");
	}
	
	@Test
	public void testSeq() throws Exception {
		testCase("1 ; 2 ; 3;;", IntType.singleton);
		testCase("true ; true ; false;;", BoolType.singleton);
		testCase("1 ; 2 ; true;;", BoolType.singleton);
		testNegativeCase("1 ; 2 ; 3;;", BoolType.singleton);
		testNegativeCase("1 ; 2 ; true;;", IntType.singleton);
	}
	
	@Test
	public void testIfThenElse() throws Exception {
		testCase("if true then 1 else 2 end;;", IntType.singleton);
		testCase("if false then 1 else 2 end;;", IntType.singleton);
		testCase("if true then false else true end;;", BoolType.singleton);
		testExceptionCase("if true then 1 else false end;;");
		testExceptionCase("if false then true then 1 end;;");
		testExceptionCase("if 1 then true else false end;;");
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("1+2*3 > 1/1*1;;", BoolType.singleton);
		testCase("1+2*3+2/2;;", IntType.singleton);
		testCase("true || false != false && false;;", BoolType.singleton);
		testCase("(20+20)/(4*5);;", IntType.singleton);
        testCase("decl x = var(0) in x := 1; *x end;;", IntType.singleton);
        testCase("decl x = var(0) in decl y = x in x := 1; *y end end;;", IntType.singleton);
        testCase("decl x = var(0) in decl y = var(0) in x := 1; *y end end;;", IntType.singleton);
        testCase("decl x = var(0) in decl y = var(0) in while *x < 10 do y := *y + 2; x := *x + 1 end; *y end end;;", IntType.singleton);
        testCase("decl x = var(3) in "
                + "decl y = var(1) in "
                + "while *x > 0 do "
                + "  y := *y * *x; "
                + "  x := *x - 1 "
                + "end; "
                + "*y "
                + "end "
                + "end;;", IntType.singleton);
        testCase("decl x = var(3) in\n"
                + "decl y = var(1) in\n"
                + "while *x > 0 do\n"
                + "  y := *y * *x;\n"
                + "  x := *x - 1\n"
                + "end;\n"
                + "*y\n"
                + "end\n"
                + "end;;", IntType.singleton);
        testCase("decl x = 2;var(0;1) in (2;x) := *x+1; *x end;;", IntType.singleton);
        testCase("***var(var(var(1)));;", IntType.singleton);	
        testCase("decl x = var(3) in "
                + "decl y = var(1) in "
                + "while *x > 0 do "
                + "  y := *y * *x; "
                + "  x := *x - 1 "
                + "end; "
                + "*y > *x "
                + "end "
                + "end;;", BoolType.singleton);
		testNegativeCase("(20+20)/(4*5);;", BoolType.singleton);
		testNegativeCase("true || false != false && false;;", IntType.singleton);
		testExceptionCase("1+2 && true != false;;");
	}
	
}