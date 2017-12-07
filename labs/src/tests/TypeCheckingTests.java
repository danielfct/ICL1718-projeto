package tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import main.Console;
import parser.ParseException;
import types.BoolType;
import types.FunType;
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
		testCase("var(0) := var(0) := 1;;", IntType.singleton);
		testNegativeCase("var(0) := 1;;", new RefType(BoolType.singleton));
		testNegativeCase("var(0) := 1;;", BoolType.singleton);
		testExceptionCase("x := 1;;");
		testExceptionCase("var(0) := true;;");
		testExceptionCase("var(var(0)) := 0");
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
	public void testFun() throws Exception {
		testCase("fun x:int -> x*x end;;", new FunType(Collections.singletonList(IntType.singleton), IntType.singleton));
		testCase("fun x:int, y:bool -> if y then x + 1 else x - 1 end end;;", new FunType(Arrays.asList(IntType.singleton, BoolType.singleton), IntType.singleton));
		testCase("fun -> true end;;", new FunType(Collections.emptyList(), BoolType.singleton));
		testCase("decl x = fun x:int -> x*2 end in x end;;", new FunType(Collections.singletonList(IntType.singleton), IntType.singleton));
		testCase("fun x:int -> x end;;", new FunType(Arrays.asList(IntType.singleton), IntType.singleton));
		testNegativeCase("fun x:int -> x end;;", new FunType(Arrays.asList(BoolType.singleton), IntType.singleton));
		testNegativeCase("fun x:int -> x end;;", new FunType(Arrays.asList(IntType.singleton), BoolType.singleton));
		testExceptionCase("fun x:funt -> x end;;");
		testExceptionCase("fun x:int, x:int -> x+x end;;");
		testExceptionCase("fun -> x end;;");
	}

	@Test
	public void testCall() throws Exception {
		testCase("fun x:int -> x+2 end (4);;", IntType.singleton);
		testCase("decl f = fun x:int -> x+1 end in f(1) end;;", IntType.singleton);
		testCase("decl x=1 in " 
				+ "		decl f = fun y:int -> y+x end in "
				+ "			decl g = fun x:int -> x+f(x) end in " 
				+ "				g(2) " 
				+ "			end "
				+ "		end " 
				+ "end;;",
				IntType.singleton);
		testCase("decl f = fun x:int -> x+1 end in " 
				+ "		decl g = fun y:int -> f(y)+2 end in "
				+ "			decl x = g(2) in " 
				+ "				x+x " 
				+ "			end " 
				+ "		end " 
				+ "end;;", 
				IntType.singleton);
		testCase("decl x = fun x:int -> var(x) end in " 
				+ "		*x(1) " 
				+ "end;;",
				IntType.singleton);
		testCase("decl f = fun y:int -> y+1 end in "
				+ "		decl g = fun x:int -> f(x)+1 end in "
				+ "			decl h = g i = fun y:int -> fun x:int -> x end (g(y)) end in "
				+ "				i(f(1)) "
				+ "			end "
				+ "		end "
				+ "end;;",
				IntType.singleton);
		testCase("decl x = 1 in "
				+ "		decl f = fun y:int -> y+x end in "
				+ "			decl g = fun h:funt(int int) -> h(x)+1 end in "
				+ "				g(f) "
				+ "			end "
				+ "		end "		
				+ "end;;",
				IntType.singleton);
		testCase("decl y = 3 in " 
				+ "		decl x = 2*y in " 
				+ "			decl f = fun y:int -> y+x end in "
				+ "				decl g = fun x:int -> fun h:funt(int int) -> x+h(x) end end in " 
				+ "					(g(2))(f) "
				+ "				end " 
				+ "			end " 
				+ "		end " 
				+ "end;;",
				IntType.singleton);
		testCase("decl comp = fun f:funt(int int), g:funt(int int) -> fun x:int -> f(g(x)) end end in " 
				+ "		decl inc = fun x:int -> x+1 end in "
				+ "			decl dup = comp(inc, inc) in " 
				+ "				dup(2) " 
				+ "			end "
				+ "		end " 
				+ "end;;",
				IntType.singleton);
		testCase("decl f = fun x:int, y:int -> "
				+ "			decl i = var(y) res = var(0) in "
				+ "				while *i > 0 do "
				+ "					res := *res + x * y; "
				+ "					i := *i - 1 "
				+ "				end;"
				+ "				*res "
				+ "			end " 	
				+ "		end "
				+ "in "
				+ "		f(3, 2) "
				+ "end;;",
				IntType.singleton);
		testCase("decl add = fun x:int, y:int -> x + y end in "
				+ "		add(1, 2) "
				+ "end;;", 
				IntType.singleton);
		testCase("decl and = fun x:bool, y:bool -> x && y end in "
				+ "		and(true, true) "
				+ "end;;", 
				BoolType.singleton);
//		testCase("decl TRUE = fun -> true end () in TRUE end;;", BoolType.singleton);
//		testCase("decl FALSE = fun -> true end () in FALSE end;;", BoolType.singleton);
		testCase("decl x = 1 in "
				+ "		fun x:int -> x+1 end (1) "
				+ "end;;", IntType.singleton);
		testCase("fun x:int -> x + 1 end (1);;", IntType.singleton);
		testCase("fun x:int -> (fun y:int -> x + y end) (2) end (1);;", IntType.singleton);
		testCase("decl x = 1 in "
				+ "		decl f = fun y:int -> y+x end in " 
				+ "			decl g = fun h:funt(int int) -> h(2) end in "
				+ "				g(f) "
				+ "			end "
				+ "     end "
				+ "	end;;", IntType.singleton);
		//		testCase("decl f = fun -> 1 end in "
		//				+ "		f() + 1 "
		//				+ "end;;", 2);
		testCase("decl f = fun x:int -> x + 1 end in "
				+ "		f(1) "
				+ "end;;", IntType.singleton);    
		//		testCase("(fun f:funt(int int int) -> f(2, 3) end) (fun x:int, y:int -> x+y end);;", 5);
		//		testCase("decl f = (fun f:funt(int int int) -> f(2, 3) end) in "
		//				+ "		f(fun x:int, y:int -> x+y end) "
		//				+ "end;;", 5);
		testCase("decl f = fun x:int -> x + 1 end in "
				+ "		decl g = fun f:funt(int int) -> f(1) end in "
				+ " 		g(f) "
				+ "		end "
				+ "end;;", IntType.singleton);
		testCase("decl f = fun x : int, y : int -> x + y end in "
				+ "		f(1, 2) + f(1, 3) "
				+ "end;;", IntType.singleton);
		testCase("decl f = fun x:int -> fun y:int -> x + y end end in "
				+ "		decl g = f(1) in "
				+ "			g(2) + g(3) "
				+ "		end "
				+ "end;;", IntType.singleton);
		testNegativeCase("decl f = fun x -> x+1 end in "
				+ "				f(1) "
				+ "		  end;;", 
				BoolType.singleton);
		 testExceptionCase("decl f = fun x -> x(x) end in f(f) end;;");
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
		testCase("decl x = var(0) in decl y = var(0) in while *x < 10 do y := *y + 2; x := *x + 1 end; *y end end;;",
				IntType.singleton);
		testCase("decl x = var(3) in " + "		decl y = var(1) in " + "			while *x > 0 do "
				+ "  			y := *y * *x; " + "  			x := *x - 1 " + "			end; " + "			*y "
				+ "		end " + "end;;", IntType.singleton);
		testCase("decl x = var(3) in\n" + "		decl y = var(1) in\n" + "			while *x > 0 do\n"
				+ "  			y := *y * *x;\n" + "  			x := *x - 1\n" + "			end;\n" + "			*y\n"
				+ "		end\n" + "end;;", IntType.singleton);
		testCase("decl x = 2;var(0;1) in " + "		(2;x) := *x+1; " + "		*x " + "end;;", IntType.singleton);
		testCase("***var(var(var(1)));;", IntType.singleton);
		testCase("decl x = var(3) in " + "		decl y = var(1) in " + "			while *x > 0 do "
				+ "  			y := *y * *x; " + "  			x := *x - 1 " + "			end; "
				+ "			*y > *x " + "		end " + "end;;", BoolType.singleton);
		testNegativeCase("(20+20)/(4*5);;", BoolType.singleton);
		testNegativeCase("true || false != false && false;;", IntType.singleton);
		testExceptionCase("1+2 && true != false;;");
		testExceptionCase("(20+20)(4*5);;");
		testCase("decl x = 1 in x + 1 end == (fun x:int -> x+1 end)(1);;", BoolType.singleton);
		testCase("decl x = 1 in x + 1 end == decl x = (fun x:int -> x+1 end) in x(1) end;;", BoolType.singleton);
		testCase("(fun x:int -> x+1 end)(1) == decl x = (fun x:int -> x+1 end) in x(1) end;;", BoolType.singleton);
	}

}