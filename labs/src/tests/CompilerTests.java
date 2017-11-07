package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import main.Compiler;

public class CompilerTests {

	// Directory in which jasmin.jar resides
	private static final File dir = new File(System.getProperty("user.dir") + "/src/");
	
	// Define true and false values
	private static final int TRUE = 1;
	private static final int FALSE = 0;

	private String getResult(String expression) throws IOException, InterruptedException {
		
		Process p;

		p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "del *.j *.class"}, null, dir);
		p.waitFor();

		System.out.println("Compiling to Jasmin source code");
		Compiler.compile(expression);

		System.out.println("Compiling to Jasmin bytecode");
		p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "java -jar jasmin.jar *.j"}, null, dir);
		p.waitFor();

		assertTrue("Compiled to Jasmin bytecode", p.exitValue() == 0);

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		StringBuffer output = new StringBuffer();
		String line = "";			
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		System.out.print(output.toString());

		p = Runtime.getRuntime().exec(new String[] {"cmd","/c", "java Demo"}, null, dir);
		p.waitFor();

		reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		output = new StringBuffer();
		line = "";			
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		System.out.print("Input: " + expression);
		System.out.print("Output: " + output.toString() + "\n\n");

		return output.toString();
		
	}

	private void testCase(String expression, int value) throws IOException, InterruptedException {
		assertTrue(getResult(expression).equals(value + "\n"));
	}
	
	private void testNegativeCase(String expression, int value) throws IOException, InterruptedException {
		assertFalse(getResult(expression).equals(value + "\n"));
	}
	
	@Test
	public void testAdd() throws Exception {
		System.out.println("\n====== ADD ======\n");
		testCase("1+2\n", 3);
		testCase("1+2+3\n", 6);
		testCase("(1+2)+4\n", 7);
		testCase("1+(2+4)+5\n", 12);
		testNegativeCase("1+1\n", 0);
		testNegativeCase("1+2\n", 0);
		testNegativeCase("(1+2)+1\n", 0);
	}
	
	@Test
	public void testAnd() throws Exception {
		System.out.println("\n====== AND ======\n");
		testCase("true && true\n", TRUE);
		testCase("true && false\n", FALSE);
		testCase("false && false\n", FALSE);
		testCase("true && (true && false)\n", FALSE);
		testNegativeCase("true && true\n", FALSE);
		testNegativeCase("true && false\n", TRUE);
		testNegativeCase("false && false\n", TRUE);
		testNegativeCase("true && (true && false)\n", TRUE);
	}
	
	@Test
	public void testBool() throws Exception {
		System.out.println("\n====== BOOL ======\n");
		testCase("true\n", TRUE);
		testCase("false\n", FALSE);
		testNegativeCase("true\n", FALSE);
		testNegativeCase("false\n", TRUE);
	}
	
	@Test
	public void testDiv() throws Exception {
		System.out.println("\n====== DIV ======\n");
		testCase("1/1\n", 1);
		testCase("6/3/2\n", 1);
		testCase("4/2/2\n", 1);
		testNegativeCase("1/1\n", 0);
	}
	
	@Test
	public void testEqual() throws Exception {
		System.out.println("\n====== EQUAL ======\n");
		testCase("1 == 1\n", TRUE);
		testCase("1 == 2\n", FALSE);
		testCase("true == true\n", TRUE);
		testCase("true == false\n", FALSE);
		testNegativeCase("1 == 1\n", FALSE);
		testNegativeCase("true == true\n", FALSE);
	}
	
	@Test
	public void testGreater() throws Exception {
		System.out.println("\n====== GREATER ======\n");
		testCase("2 > 1\n", TRUE);
		testCase("3 > 2 && 2 > 1\n", TRUE);
		testCase("3 > 1 && 1 > 2\n", FALSE);
		testNegativeCase("10 > 1\n", FALSE);
		testNegativeCase("1 > 10\n", TRUE);
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		System.out.println("\n====== GREATER OR EQUAL ======\n");
		testCase("1 >= 1\n", TRUE);
		testCase("2 >= 1\n", TRUE);
		testCase("1 >= 2\n", FALSE);
		testNegativeCase("1 >= 1\n", FALSE);
		testNegativeCase("1 >= 2\n", TRUE);
	}
	
	@Test
	public void testLesser() throws Exception {
		System.out.println("\n====== LESSER ======\n");
		testCase("1 < 2\n", TRUE);
		testCase("1 < 2 && 2 < 3\n", TRUE);
		testCase("2 < 1\n", FALSE);
		testCase("1 < 2 && 2 < 1\n", FALSE);
		testNegativeCase("1 < 2\n", FALSE);
		testNegativeCase("1 < 2 && 2 < 1\n", TRUE);
	}
	
	@Test
	public void testLesserEq() throws Exception {
		System.out.println("\n====== LESSER OR EQUAL ======\n");
		testCase("1 <= 1\n", TRUE);
		testCase("1 <= 2\n", TRUE);
		testCase("2 <= 1\n", FALSE);
		testNegativeCase("1 <= 1\n", FALSE);
	}
	
	@Test
	public void testMul() throws Exception {
		System.out.println("\n====== MUL ======\n");
		testCase("4*2\n", 8);
		testCase("4*2*2\n", 16);
		testNegativeCase("1*1\n", 0);
		testNegativeCase("1*1*0\n", 1);
	}
	
	@Test
	public void testNot() throws Exception {
		System.out.println("\n====== NOT ======\n");
		testCase("!false\n", TRUE);
		testCase("!true\n", FALSE);
		testCase("!(false)\n", TRUE);
		testCase("!(false && true)\n", TRUE);
		testCase("!(false || true)\n", FALSE);
		testNegativeCase("!false\n", FALSE);
		testNegativeCase("!true\n", TRUE);
	}
	
	@Test
	public void testNotEqual() throws Exception {
		System.out.println("\n====== NOT EQUAL ======\n");
		testCase("1 != 2\n", TRUE);
		testCase("1 != 1\n", FALSE);
		testCase("true != false\n", TRUE);
		testCase("true != true\n", FALSE);
		testNegativeCase("1 != 2\n", FALSE);
		testNegativeCase("true != true\n", TRUE);
	}
	
	@Test
	public void testNum() throws Exception {
		System.out.println("\n====== NUM ======\n");
		testCase("1\n", 1);
		testCase("10\n", 10);
		testCase("-1\n", -1);
		testNegativeCase("1\n", 0);
	}
	
	@Test
	public void testOr() throws Exception {
		System.out.println("\n====== OR ======\n");
		testCase("true || true\n", TRUE);
		testCase("true || false\n", TRUE);
		testCase("false || true\n", TRUE);
		testCase("false || false\n", FALSE);
		testNegativeCase("true || true\n", FALSE);
		testNegativeCase("true || false\n", FALSE);
		testNegativeCase("false || true\n", FALSE);
		testNegativeCase("false || false\n", TRUE);
	}
	
	@Test
	public void testSub() throws Exception {
		System.out.println("\n====== SUB ======\n");
		testCase("1-1\n", 0);
		testCase("1-2-3\n", -4);
		testNegativeCase("1-1\n", -1);
		testNegativeCase("1-1-1\n", 0);
	}
	
	@Test
	public void testDecl() throws Exception {
		System.out.println("\n====== DECL ======\n");
		testCase("decl x = 1 in x+1 end\n", 2);
		testCase("decl x = 1 in decl y = 2 in x+y end end\n", 3);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n", 6);
		testCase("decl x = decl x = 1 in x+2 end y = decl x = 34 in x/17 end in decl z = 3 in (x+y)*z end end\n", 15);
		testCase("decl y = decl x = 3 in x * 4 end in y + 2 end\n", 14);
		testCase("decl x = 6 in decl x = 3 y = decl z = 7 + x in z * x end in y + x end end\n", 81);
		testCase("decl x = 6 in decl x = decl z = 2 in z * z * z end y = decl z = 7 + x in z * x end in y + x end end\n", 86);
		testCase("decl x = true in x && false end\n", FALSE);
		testCase("decl x = false in decl y = true in x || y end end\n", TRUE);
		testCase("decl x = decl y = 3 in y != 2 end in x && true end\n", TRUE);
		testCase("decl x = 1 in x < 2 end && decl y = 1 in 2 <= y end\n", FALSE);
		testCase("decl x = decl x = 1 in x >= 2 end y = decl x = 34 in x*17 end in decl z = 3 in !x && (y == z) end end\n", FALSE);
		testCase("decl x = !true y = false in !(x || y) end\n", TRUE);
		testCase("decl x = decl z = 2 in !(2 <= z) end y = !true in !(x || y) end\n", TRUE);
		testCase("decl x = !true y = decl z = 2 in !(2 <= z) end in decl w = 2 t = !y in (w != 1) && !(x || y) || t end end\n", TRUE);
		testNegativeCase("decl x = 1 in x+1 end\n", 0);
	}
	
	@Test
	public void testMixed() throws Exception {
		System.out.println("\n====== MIXED ======\n");
		testCase("1+2*3 > 1/1*1\n", TRUE);
		testCase("1+2*3+2/2\n", 8);
		testCase("true || false != false && false\n", TRUE);
		testCase("(20+20)/(4*5)\n", 2);
	}
	
}
