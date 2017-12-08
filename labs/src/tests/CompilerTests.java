package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import compiler.IdFactory;
import main.Compiler;

public class CompilerTests {

	// Directory in which jasmin.jar resides
	private static final File dir = new File(System.getProperty("user.dir"));

	private String getResult(String expression) throws IOException, InterruptedException {

		Process p;

		p = Runtime.getRuntime().exec(new String[] { "cmd", "/c", "del *.j *.class" }, null, dir);
		p.waitFor();

		System.out.println("Compiling to Jasmin source code");
		Compiler.compile(expression);

		System.out.println("Compiling to Jasmin bytecode");
		p = Runtime.getRuntime().exec(new String[] { "cmd", "/c", "java -jar jasmin.jar *.j" }, null, dir);
		p.waitFor();

		assertTrue("Compiled to Jasmin bytecode", p.exitValue() == 0);

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		StringBuffer output = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		System.out.print(output.toString());

		p = Runtime.getRuntime().exec(new String[] { "cmd", "/c", "java Main" }, null, dir);
		p.waitFor();

		reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		output = new StringBuffer();
		line = "";
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		System.out.println("Input: " + expression);
		System.out.println("Output: " + output.toString() + "\n");

		return output.toString();

	}

	private void testCase(String expression, Object value) throws IOException, InterruptedException {
		assertTrue(getResult(expression).equals(value + "\n"));
	}

	private void testNegativeCase(String expression, Object value) throws IOException, InterruptedException {
		assertFalse(getResult(expression).equals(value + "\n"));
	}

	@Test
	public void testAdd() throws Exception {
		System.out.println("\n====== ADD ======\n");
		testCase("1+2;;", 3);
		testCase("1+2+3;;", 6);
		testCase("(1+2)+4;;", 7);
		testCase("1+(2+4)+5;;", 12);
		testNegativeCase("1+1;;", 0);
		testNegativeCase("1+2;;", 0);
		testNegativeCase("(1+2)+1;;", 0);
	}

	@Test
	public void testAnd() throws Exception {
		System.out.println("\n====== AND ======\n");
		testCase("true && true;;", true);
		testCase("true && false;;", false);
		testCase("false && false;;", false);
		testCase("true && (true && false);;", false);
		testNegativeCase("true && true;;", false);
		testNegativeCase("true && false;;", true);
		testNegativeCase("false && false;;", true);
		testNegativeCase("true && (true && false);;", true);
	}

	@Test
	public void testBool() throws Exception {
		System.out.println("\n====== BOOL ======\n");
		testCase("true;;", true);
		testCase("false;;", false);
		testNegativeCase("true;;", false);
		testNegativeCase("false;;", true);
	}

	@Test
	public void testDiv() throws Exception {
		System.out.println("\n====== DIV ======\n");
		testCase("1/1;;", 1);
		testCase("6/3/2;;", 1);
		testCase("4/2/2;;", 1);
		testNegativeCase("1/1;;", 0);
	}

	@Test
	public void testEqual() throws Exception {
		System.out.println("\n====== EQUAL ======\n");
		testCase("1 == 1;;", true);
		testCase("1 == 2;;", false);
		testCase("true == true;;", true);
		testCase("true == false;;", false);
		testNegativeCase("1 == 1;;", false);
		testNegativeCase("true == true;;", false);
	}

	@Test
	public void testGreater() throws Exception {
		System.out.println("\n====== GREATER ======\n");
		testCase("2 > 1;;", true);
		testCase("3 > 2 && 2 > 1;;", true);
		testCase("3 > 1 && 1 > 2;;", false);
		testNegativeCase("10 > 1;;", false);
		testNegativeCase("1 > 10;;", true);
	}

	@Test
	public void testGreaterEq() throws Exception {
		System.out.println("\n====== GREATER OR EQUAL ======\n");
		testCase("1 >= 1;;", true);
		testCase("2 >= 1;;", true);
		testCase("1 >= 2;;", false);
		testNegativeCase("1 >= 1;;", false);
		testNegativeCase("1 >= 2;;", true);
	}

	@Test
	public void testLesser() throws Exception {
		System.out.println("\n====== LESSER ======\n");
		testCase("1 < 2;;", true);
		testCase("1 < 2 && 2 < 3;;", true);
		testCase("2 < 1;;", false);
		testCase("1 < 2 && 2 < 1;;", false);
		testNegativeCase("1 < 2;;", false);
		testNegativeCase("1 < 2 && 2 < 1;;", true);
	}

	@Test
	public void testLesserEq() throws Exception {
		System.out.println("\n====== LESSER OR EQUAL ======\n");
		testCase("1 <= 1;;", true);
		testCase("1 <= 2;;", true);
		testCase("2 <= 1;;", false);
		testNegativeCase("1 <= 1;;", false);
	}

	@Test
	public void testMul() throws Exception {
		System.out.println("\n====== MUL ======\n");
		testCase("4*2;;", 8);
		testCase("4*2*2;;", 16);
		testNegativeCase("1*1;;", 0);
		testNegativeCase("1*1*0;;", 1);
	}

	@Test
	public void testNot() throws Exception {
		System.out.println("\n====== NOT ======\n");
		testCase("!false;;", true);
		testCase("!true;;", false);
		testCase("!(false);;", true);
		testCase("!(false && true);;", true);
		testCase("!(false || true);;", false);
		testNegativeCase("!false;;", false);
		testNegativeCase("!true;;", true);
	}

	@Test
	public void testNotEqual() throws Exception {
		System.out.println("\n====== NOT EQUAL ======\n");
		testCase("1 != 2;;", true);
		testCase("1 != 1;;", false);
		testCase("true != false;;", true);
		testCase("true != true;;", false);
		testNegativeCase("1 != 2;;", false);
		testNegativeCase("true != true;;", true);
	}

	@Test
	public void testNum() throws Exception {
		System.out.println("\n====== NUM ======\n");
		testCase("1;;", 1);
		testCase("10;;", 10);
		testCase("-1;;", -1);
		testNegativeCase("1;;", 0);
	}

	@Test
	public void testOr() throws Exception {
		System.out.println("\n====== OR ======\n");
		testCase("true || true;;", true);
		testCase("true || false;;", true);
		testCase("false || true;;", true);
		testCase("false || false;;", false);
		testNegativeCase("true || true;;", false);
		testNegativeCase("true || false;;", false);
		testNegativeCase("false || true;;", false);
		testNegativeCase("false || false;;", true);
	}

	@Test
	public void testSub() throws Exception {
		System.out.println("\n====== SUB ======\n");
		testCase("1-1;;", 0);
		testCase("1-2-3;;", -4);
		testNegativeCase("1-1;;", -1);
		testNegativeCase("1-1-1;;", 0);
	}

	@Test
	public void testDecl() throws Exception {
		System.out.println("\n====== DECL ======\n");
		IdFactory.singleton.init();
		testCase("decl x = 1 in x+1 end;;", 2);
		testCase("decl x = 1 in decl y = 2 in x+y end end;;", 3);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end;;", 6);
		testCase("decl x = decl x = 1 in x+2 end y = decl x = 34 in x/17 end in decl z = 3 in (x+y)*z end end;;", 15);
		testCase("decl y = decl x = 3 in x * 4 end in y + 2 end;;", 14);
		testCase("decl x = 6 in decl x = 3 y = decl z = 7 + x in z * x end in y + x end end;;", 81);
		testCase("decl x = 6 in decl x = decl z = 2 in z * z * z end y = decl z = 7 + x in z * x end in y + x end end;;", 86);
		testCase("decl x = true in x && false end;;", false);
		testCase("decl x = false in decl y = true in x || y end end;;", true);
		testCase("decl x = decl y = 3 in y != 2 end in x && true end;;", true);
		testCase("decl x = 1 in x < 2 end && decl y = 1 in 2 <= y end;;", false);
		testCase("decl x = decl x = 1 in x >= 2 end y = decl x = 34 in x*17 end in decl z = 3 in !x && (y == z) end end;;", false);
		testCase("decl x = !true y = false in !(x || y) end;;", true);
		testCase("decl x = decl z = 2 in !(2 <= z) end y = !true in !(x || y) end;;", true);
		testCase("decl x = !true y = decl z = 2 in !(2 <= z) end in decl w = 2 t = !y in (w != 1) && !(x || y) || t end end;;", true);
		testNegativeCase("decl x = 1 in x+1 end;;", 0);
	}

	@Test
	public void testAssign() throws Exception {
		System.out.println("\n====== ASSIGN ======\n");
		IdFactory.singleton.init();
		testCase("var(1) := 0;;", 0);
		testCase("var(0) := var(0) := 1;;", 1);
		testCase("var(var(1)) := var(2);;", "Ref_3 int");
		testCase("var(var(true)) := var(false);;", "Ref_5 bool");
		testNegativeCase("var(0) := 1;;", 2);
		testNegativeCase("var(true) := false;;", true);
	}

	@Test
	public void testDeref() throws Exception {
		System.out.println("\n====== DEREF ======\n");
		IdFactory.singleton.init();
		testCase("*var(0);;", 0);
		testCase("*var(99);;", 99);
		testCase("*var(true);;", true);
		testCase("*var(*var(1));;", 1);
		testCase("*var(var(0));;", "Ref_5 int");
		testCase("*var(var(var(0)));;", "Ref_7 Ref(int)");
		testCase("*var(*var(true));;", true);
		testCase("***var(var(var(1)));;", 1);
		testNegativeCase("*var(0);;", 1);
		testNegativeCase("*var(true);;", 0);
	}

	@Test
	public void testVar() throws Exception {
		System.out.println("\n====== VAR ======\n");
		IdFactory.singleton.init();
		testCase("var(0);;", "Ref_0 int");
		testCase("var(true);;", "Ref_1 bool");
		testNegativeCase("var(false);;", "Ref_2 int");
		testNegativeCase("var(1);;", 1);
	}

	@Test
	public void testWhile() throws Exception {
		System.out.println("\n====== WHILE ======\n");
		IdFactory.singleton.init();
		// by default, while statement always returns false
		testCase("decl x = var(0) in while *x < 1 do x := *x + 1 end end;;", false);
		testCase("decl i = var(1) in " 
				+ "		decl x = while *i > 0 do " 
				+ "      			i := *i - 1 "
				+ "           	 end " 
				+ "		in " 
				+ "			x " 
				+ "		end " 
				+ "end;;", false);
		testNegativeCase("decl x = var(0) in while *x < 1 do x := *x + 1 end end;;", true);
		testNegativeCase("decl x = var(0) in while *x < 1 do x := *x + 1 end end;;", 2);
	}

	@Test
	public void testSeq() throws Exception {
		System.out.println("\n====== SEQUENCE ======\n");
		IdFactory.singleton.init();
		testCase("1 ; 2 ; 3;;", 3);
		testCase("true ; true ; false;;", false);
		testCase("1 ; 2 ; true;;", true);
		testCase("decl x = 1; 2 in x end;;", 2);
		testCase("var(0); var(1);;", "Ref_0 int");
		testNegativeCase("1 ; 2 ; 3;;", 1);
		testNegativeCase("1 ; 2 ; true;;", false);
	}

	@Test
	public void testIfThenElse() throws Exception {
		System.out.println("\n====== IF THEN ELSE ======\n");
		IdFactory.singleton.init();
		testCase("if true then 1 else 0 end;;", 1);
		testCase("if false then 1 else 0 end;;", 0);
		testCase("if true then false else true end;;", false);
		testCase("if false then false else true end;;", true);
		testCase("if true then var(1) else var(2) end;;", "Ref_0 int");
		testNegativeCase("if true then false else true end;;", true);
		testNegativeCase("if false then false else true end;;", false);
		testNegativeCase("if true then false else true end;;", 1);
		testNegativeCase("if false then false else true end;;", 2);
	}

	@Test
	public void testFun() throws Exception {
		System.out.println("\n====== FUNCTION DECL ======\n");
		IdFactory.singleton.init();
		//TODO
	}

	@Test
	public void testCall() throws Exception {
		System.out.println("\n====== FUNCTION CALL ======\n");
		testCase("decl x = 1 in "
				+ "		fun x:int -> x+1 end (1) "
				+ "end;;", 
				2);
		testCase("fun x:int -> x + 1 end (1);;", 2);
		testCase("fun x:int -> (fun y:int -> x + y end) (2) end (1);;", 3);
		testCase("decl x = 1 in "
				+ "		decl f = fun y:int -> y+x end in " 
				+ "			decl g = fun h:funt(int, int) -> h(2) end in "
				+ "				g(f) "
				+ "			end "
				+ "     end "
				+ "	end;;", 
				3);
		testCase("decl f = fun -> 1 end in "
				+ "		f() + 1 "
				+ "end;;", 
				2);
		testCase("decl f = fun x:int -> x + 1 end in "
				+ "		f(1) "
				+ "end;;", 
				2);    
		testCase("(fun f:funt(int, int, int) -> f(2, 3) end) (fun x:int, y:int -> x+y end);;", 5);
//		testCase("decl f = (fun f:funt(int, int, int) -> f(2, 3) end) in "
//				+ "		f(fun x:int, y:int -> x+y end) "
//				+ "end;;", 5);
		testCase("decl f = fun x:int -> x + 1 end in "
				+ "		decl g = fun f:funt(int, int) -> f(1) end in "
				+ " 		g(f) "
				+ "		end "
				+ "end;;", 
				2);
		testCase("decl f = fun x:int, y:int -> x + y end in "
				+ "		f(1, 2) + f(1, 3) "
				+ "end;;", 
				7);
		testCase("decl f = fun x:int -> fun y:int -> x + y end end in "
				+ "		decl g = f(1) in "
				+ "			g(2) + g(3) "
				+ "		end "
				+ "end;;",
				7);
		testCase("fun x:int -> x+2 end (4);;", 
				6);
		testCase("decl f = fun x:int -> x+1 end in f(1) end;;", 
				2);
		testCase("decl x=1 in " 
				+ "		decl f = fun y:int -> y+x end in "
				+ "			decl g = fun x:int -> x+f(x) end in " 
				+ "				g(2) " 
				+ "			end "
				+ "		end " 
				+ "end;;",
				5);
		testCase("decl f = fun x:int -> x+1 end in " 
				+ "		decl g = fun y:int -> f(y)+2 end in "
				+ "			decl x = g(2) in " 
				+ "				x+x " 
				+ "			end " 
				+ "		end " 
				+ "end;;", 
				10);
		testCase("decl x = fun x:int -> var(x) end in " 
				+ "		*x(1) " 
				+ "end;;",
				1);
		testCase("decl f = fun y:int -> y+1 end in "
				+ "		decl g = fun x:int -> f(x)+1 end in "
				+ "			decl h = g i = fun y:int -> fun x:int -> x end (g(y)) end in "
				+ "				i(f(1)) "
				+ "			end "
				+ "		end "
				+ "end;;",
				4);
		testCase("decl x = 1 in "
				+ "		decl f = fun y:int -> y+x end in "
				+ "			decl g = fun h:funt(int, int) -> h(x)+1 end in "
				+ "				g(f) "
				+ "			end "
				+ "		end "		
				+ "end;;",
				3);
		testCase("decl y = 3 in " 
				+ "		decl x = 2*y in " 
				+ "			decl f = fun y:int -> y+x end in "
				+ "				decl g = fun x:int -> fun h:funt(int, int) -> x+h(x) end end in " 
				+ "					g(2)(f) "
				+ "				end " 
				+ "			end " 
				+ "		end " 
				+ "end;;",
				10);
//		testCase("decl comp = fun f:funt(int, int), g:funt(int, int) -> fun x:int -> f(g(x)) end end in " 
//				+ "		decl inc = fun x:int -> x+1 end in "
//				+ "			decl dup = comp(inc, inc) in " 
//				+ "				dup(2) " 
//				+ "			end "
//				+ "		end " 
//				+ "end;;",
//				4);
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
				12);
		testCase("decl add = fun x:int, y:int -> x + y end in "
				+ "		add(1, 2) "
				+ "end;;", 
				3);
		testCase("decl and = fun x:bool, y:bool -> x && y end in "
				+ "		and(true, true) "
				+ "end;;", 
				true);
		testCase("fun y:int -> (fun x:int -> x end)(y) end (1);;", 1);
		testCase("(fun x:int -> (fun h:funt(int, int) -> h(x) end) end)(1)(fun x:int -> x+1 end);;", 2);
		testNegativeCase("decl f = fun x:int -> x+1 end in (1) end;;", 2);
	}

	@Test
	public void testMixed() throws Exception {
		System.out.println("\n====== MIXED ======\n");
		IdFactory.singleton.init();
		testCase("1+2*3 > 1/1*1;;", true);
		testCase("1+2*3+2/2;;", 8);
		testCase("true || false != false && false;;", true);
		testCase("(20+20)/(4*5);;", 2);
		// TODO add imperative testes
		testCase("decl x = 1 in x + 1 end == (fun x:int -> x+1 end)(1);;", true);
		testCase("decl x = 1 in x + 1 end == decl x = (fun x:int -> x+1 end) in x(1) end;;", true);
		testCase("(fun x:int -> x+1 end)(1) == decl x = (fun x:int -> x+1 end) in x(1) end;;", true);
	}

}
