package tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import main.Console;
import memory.MemoryCell;
import parser.ParseException;
import types.BoolType;
import types.IntType;
import values.BoolValue;
import values.ClosureValue;
import values.IValue;
import values.IntValue;
import ast.ASTAdd;
import ast.ASTBool;
import ast.ASTFun.Parameter;
import ast.ASTId;
import ast.ASTIfThenElse;
import ast.ASTMul;
import ast.ASTNum;
import ast.ASTSub;
import environment.Environment;
import environment.IEnvironment;

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
		// by default, while statement always returns false
		testCase("decl x = var(0) in "
				+ "		while *x < 1 do "
				+ "			x := *x + 1 "
				+ "		end "
				+ "end;;", 
				new BoolValue(false));
		testCase("decl i = var(1) in " 
				+ "		decl x = while *i > 0 do " 
				+ "					i := *i - 1 "
				+ "           	 end " 
				+ "		in " 
				+ "			x " 
				+ "		end " 
				+ "end;;",
				new BoolValue(false));
		testNegativeCase("decl x = var(0) in "
				+ "				while *x < 1 do "
				+ "					x := *x + 1 "
				+ "				end "
				+ "		end;;", 
				new BoolValue(true));
		testNegativeCase("decl x = var(0) in "
				+ "				while *x < 1 do "
				+ "					x := *x + 1 "
				+ "				end "
				+ "		end;;", 
				new IntValue(0));
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
		testCase("if true then "
				+ "		1 "
				+ "else "
				+ "		0 "
				+ "end;;", 
				new IntValue(1));
		testCase("if false then "
				+ "		1 "
				+ "else "
				+ "		0 "
				+ "end;;", 
				new IntValue(0));
		testCase("if true then "
				+ "		false "
				+ "else "
				+ "		true "
				+ "end;;", 
				new BoolValue(false));
		testCase("if false then "
				+ "		false "
				+ "else "
				+ "		true "
				+ "end;;", 
				new BoolValue(true));
		testNegativeCase("if true then "
				+ "				false "
				+ "		  else "
				+ "				true "
				+ "		  end;;", 
				new BoolValue(true));
		testNegativeCase("if false then "
				+ "			  	false "
				+ "		  else "
				+ "			  	true "
				+ "		  end;;", 
				new BoolValue(false));
		testNegativeCase("if true then "
				+ "				false "
				+ "		  else "
				+ "				true "
				+ "		  end;;", 
				new IntValue(1));
		testNegativeCase("if false then "
				+ "				false "
				+ "		  else "
				+ "				true "
				+ "		  end;;", 
				new IntValue(2));
	}

	@Test
	public void testFun() throws Exception {
		IEnvironment<IValue> emptyEnv = new Environment<IValue>();
		testCase("fun -> x end;;", 
				new ClosureValue(
						Collections.emptyList(), 
						new ASTId("x"), 
						emptyEnv
						)
				);
		testCase("fun x:int -> x*x end;;", 
				new ClosureValue(
						Collections.singletonList(new Parameter("x", IntType.singleton)),
						new ASTMul(new ASTId("x"), new ASTId("x")), 
						emptyEnv
						)
				);
		testCase("fun x:int, y:bool -> if y then x + 1 else x - 1 end end;;",
				new ClosureValue(
						Arrays.asList(new Parameter("x", IntType.singleton), new Parameter("y", BoolType.singleton)),
						new ASTIfThenElse(new ASTId("y"), new ASTAdd(new ASTId("x"), new ASTNum(1)), new ASTSub(new ASTId("x"), new ASTNum(1))),
						emptyEnv
						)
				);
		testCase("fun -> true end;;",
				new ClosureValue(
						Collections.emptyList(), 
						new ASTBool(true), 
						emptyEnv
						)
				);
		testCase("decl x = fun x:int -> x*2 end in x end;;",
				new ClosureValue(
						Arrays.asList(new Parameter("x", IntType.singleton)),
						new ASTMul(new ASTId("x"), new ASTNum(2)), 
						emptyEnv
						)
				);
		testNegativeCase("fun x:int, x:int -> x+x end;;",
				new ClosureValue(
						Arrays.asList(new Parameter("x", IntType.singleton)),
						new ASTAdd(new ASTId("x"), new ASTId("x")), 
						emptyEnv
						)
				);
	}

	@Test
	public void testCall() throws Exception {
		testCase("decl x = 1 in "
				+ "		fun x:int -> x+1 end (1) "
				+ "end;;", new IntValue(2));
		testCase("fun x:int -> x + 1 end (1);;", new IntValue(2));
		testCase("fun x:int -> (fun y:int -> x + y end) (2) end (1);;", new IntValue(3));
		testCase("decl x = 1 in "
				+ "		decl f = fun y:int -> y+x end in " 
				+ "			decl g = fun h:funt(int, int) -> h(2) end in "
				+ "				g(f) "
				+ "			end "
				+ "     end "
				+ "	end;;", new IntValue(3));
		testCase("decl f = fun -> 1 end in "
				+ "		f() + 1 "
				+ "end;;", new IntValue(2));
		testCase("decl f = fun x:int -> x + 1 end in "
				+ "		f(1) "
				+ "end;;", new IntValue(2));    
		testCase("(fun f:funt(int, int, int) -> f(2, 3) end) (fun x:int, y:int -> x+y end);;", new IntValue(5));
		testCase("decl f = (fun f:funt(int, int, int) -> f(2, 3) end) in "
				+ "		f(fun x:int, y:int -> x+y end) "
				+ "end;;", new IntValue(5));
		testCase("decl f = fun x:int -> x + 1 end in "
				+ "		decl g = fun f:funt(int, int) -> f(1) end in "
				+ " 		g(f) "
				+ "		end "
				+ "end;;", new IntValue(2));
		testCase("decl f = fun x : int, y : int -> x + y end in "
				+ "		f(1, 2) + f(1, 3) "
				+ "end;;", new IntValue(7));
		testCase("decl f = fun x:int -> fun y:int -> x + y end end in "
				+ "		decl g = f(1) in "
				+ "			g(2) + g(3) "
				+ "		end "
				+ "end;;", new IntValue(7));
		testCase("fun x:int -> x+2 end (4);;", new IntValue(6));
		testCase("decl f = fun x:int -> x+1 end in f(1) end;;", new IntValue(2));
		testCase("decl x=1 in " 
				+ "		decl f = fun y:int -> y+x end in "
				+ "			decl g = fun x:int -> x+f(x) end in " 
				+ "				g(2) " 
				+ "			end "
				+ "		end " 
				+ "end;;",
				new IntValue(5));
		testCase("decl f = fun x:int -> x+1 end in " 
				+ "		decl g = fun y:int -> f(y)+2 end in "
				+ "			decl x = g(2) in " 
				+ "				x+x " 
				+ "			end " 
				+ "		end " 
				+ "end;;", 
				new IntValue(10));
		testCase("decl x = fun x:int -> var(x) end in " 
				+ "		*x(1) " 
				+ "end;;",
				new IntValue(1));
		testCase("decl f = fun y:int -> y+1 end in "
				+ "		decl g = fun x:int -> f(x)+1 end in "
				+ "			decl h = g i = fun y:int -> fun x:int -> x end (g(y)) end in "
				+ "				i(f(1)) "
				+ "			end "
				+ "		end "
				+ "end;;",
				new IntValue(4));
		testCase("decl x = 1 in "
				+ "		decl f = fun y:int -> y+x end in "
				+ "			decl g = fun h:funt(int, int) -> h(x)+1 end in "
				+ "				g(f) "
				+ "			end "
				+ "		end "		
				+ "end;;",
				new IntValue(3));
		testCase("decl y = 3 in " 
				+ "		decl x = 2*y in " 
				+ "			decl f = fun y:int -> y+x end in "
				+ "				decl g = fun x:int -> fun h:funt(int, int) -> x+h(x) end end in " 
				+ "					g(2)(f) "
				+ "				end " 
				+ "			end " 
				+ "		end " 
				+ "end;;",
				new IntValue(10));
		testCase("decl comp = fun f:funt(int, int), g:funt(int, int) -> fun x:int -> f(g(x)) end end in " 
				+ "		decl inc = fun x:int -> x+1 end in "
				+ "			decl dup = comp(inc, inc) in " 
				+ "				dup(2) " 
				+ "			end "
				+ "		end " 
				+ "end;;",
				new IntValue(4));
		testCase("decl f = fun x:int, y:int -> "
				+ "			decl i = var(y) res = var(0) in "
				+ "				while *i > 0 do "
				+ "					res := *res + x * y; "
				+ "					i := *i - 1 "
				+ "				end; "
				+ "				*res "
				+ "			end " 	
				+ "		end "
				+ "in "
				+ "		f(3, 2) "
				+ "end;;",
				new IntValue(12));
		testCase("decl add = fun x:int, y:int -> x + y end in "
				+ "		add(1, 2) "
				+ "end;;", 
				new IntValue(3));
		testCase("decl and = fun x:bool, y:bool -> x && y end in "
				+ "		and(true, true) "
				+ "end;;", 
				new BoolValue(true));
		testNegativeCase("decl f = fun x -> x+1 end in (1) end;;", new IntValue(1));
	}

	@Test
	public void testMixed() throws Exception {
		testCase("1+2*3 > 1/1*1;;", new BoolValue(true));
		testCase("1+2*3+2/2;;", new IntValue(8));
		testCase("true || false != false && false;;", new BoolValue(true));
		testCase("(20+20)/(4*5);;", new IntValue(2));
		testCase("3*5 != 1+2 == true;;", new BoolValue(true));
		testCase("decl x = var(0) in "
				+ "		x := 1; "
				+ "		*x "
				+ "end;;", 
				new IntValue(1));
		testCase("decl x = var(0) in "
				+ "		decl y = x in "
				+ "			x := 1; "
				+ "			*y "
				+ "		end "
				+ "end;;", 
				new IntValue(1));
		testCase("decl x = var(0) in "
				+ "		decl y = var(0) in "
				+ "			x := 1; "
				+ "			*y "
				+ "		end "
				+ "end;;", 
				new IntValue(0));
		testCase("decl x = var(0) in "
				+ "		decl y = var(0) in " 
				+ "			while *x < 10 do "
				+ "				y := *y + 2; "
				+ "				x := *x + 1 "
				+ "			end; "
				+ "			*y "
				+ "		end "
				+ "end;;",
				new IntValue(20));
		testCase("if true then "
				+ "		1 "
				+ "else "
				+ "		2 "
				+ "end;;", 
				new IntValue(1));
		testCase("if false then "
				+ "		1 "
				+ "else "
				+ "		2 "
				+ "end;;", 
				new IntValue(2));
		testCase("decl x = var(3) in " 
				+ "		decl y = var(1) in " 
				+ "			while *x > 0 do " 
				+ "  			y := *y * *x; "
				+ "  			x := *x - 1 "
				+ "			end; " 
				+ "		*y " 
				+ "		end " 
				+ "end;;", 
				new IntValue(6));
		testCase("decl x = var(3) in\n" 
				+ "		decl y = var(1) in\n" 
				+ "			while *x > 0 do\n" 
				+ "  			y := *y * *x;\n"
				+ "  			x := *x - 1\n" 
				+ "			end;\n" 
				+ "			*y\n" 
				+ "		end\n" 
				+ "end;;", 
				new IntValue(6));
		testCase("decl x = 2; var(0;1) in "
				+ "		(2; x) := *x+1; *x "
				+ "end;;", 
				new IntValue(2));
		testCase("***var(var(var(1)));;", new IntValue(1));
		int[] values = new int[] {0, 1, 7, 2, 5, 8, 16, 3, 19, 6, 14, 9, 9, 17, 17, 4, 12, 20, 20, 
				7, 7, 15, 15, 10, 23, 10, 111, 18, 18, 18, 106, 5, 26, 13, 13, 21, 21, 21, 34, 8, 
				109, 8, 29, 16, 16, 16, 104, 11, 24, 24, 24, 11, 11, 112, 112, 19, 32, 19, 32, 19, 
				19, 107, 107, 6, 27, 27, 27, 14, 14, 14, 102, 22};
		for (int i = 0; i < values.length; i++) {
			testCase("decl i = var("+(i+1)+") n = var(0) in "
					+ "		decl x = while *i > 1 do " 
					+ "					n := *n + 1; " 
					+ "             	if (*i/2)*2 == *i then "
					+ "						i := *i/2 "
					+ "             	else "
					+ "						i := *i*3 + 1 " 
					+ "             	end " 
					+ "           	end " 
					+ "  	in " 
					+ "			*n " 
					+ "  	end "
					+ "end;;", 
					new IntValue(values[i]));
		}
		testCase("decl x = 1 in x + 1 end == (fun x:int -> x+1 end)(1);;", new BoolValue(true));
		testCase("decl x = 1 in x + 1 end == decl x = (fun x:int -> x+1 end) in x(1) end;;", new BoolValue(true));
		testCase("(fun x:int -> x+1 end)(1) == decl x = (fun x:int -> x+1 end) in x(1) end;;", new BoolValue(true));
		testNegativeCase("(20+20)/(4*5);;", new IntValue(1));
		testNegativeCase("true || false != false && false;;", new BoolValue(false));
		testNegativeCase("1 == 2 || 3 == 4 && xpto;;", new BoolValue(true));
		testNegativeCase("!(1 == 2) && xpto;;", new BoolValue(true));
	}

}