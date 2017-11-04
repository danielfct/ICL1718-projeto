package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import main.Compiler;
import parser.ParseException;

public class CompilerTests {

	// Directory in which jasmin.jar resides
	private static final File dir = new File(System.getProperty("user.dir") + "/src/");
	
	// Define true and false values
	private static final int TRUE = 1;
	private static final int FALSE = 0;

	private String getResult(String expression) throws IOException, InterruptedException, ParseException, FileNotFoundException {
		
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

	private void testCase(String expression, int value) throws FileNotFoundException, IOException, InterruptedException, ParseException {
		assertTrue(getResult(expression).equals(value + "\n"));
	}
	
	private void testNegativeCase(String expression, int value) throws FileNotFoundException, IOException, InterruptedException, ParseException {
		assertFalse(getResult(expression).equals(value + "\n"));
	}
	
	@Test
	public void testAdd() throws Exception {
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
		testCase("true\n", TRUE);
		testCase("false\n", FALSE);
		testNegativeCase("true\n", FALSE);
		testNegativeCase("false\n", TRUE);
	}
	
	@Test
	public void testDiv() throws Exception {
		testCase("1/1\n", 1);
		testCase("6/3/2\n", 1);
		testCase("4/2/2\n", 1);
		testNegativeCase("1/1\n", 0);
	}
	
	@Test
	public void testEqual() throws Exception {
		testCase("1 == 1\n", TRUE);
		testCase("1 == 2\n", FALSE);
		testCase("true == true\n", TRUE);
		testCase("true == false\n", FALSE);
		testNegativeCase("1 == 1\n", FALSE);
		testNegativeCase("true == true\n", FALSE);
	}
	
	@Test
	public void testGreater() throws Exception {
		testCase("2 > 1\n", TRUE);
		testCase("3 > 2 && 2 > 1\n", TRUE);
		testCase("3 > 1 && 1 > 2\n", FALSE);
		testNegativeCase("10 > 1\n", FALSE);
		testNegativeCase("1 > 10\n", TRUE);
	}
	
	@Test
	public void testGreaterEq() throws Exception {
		testCase("1 >= 1\n", TRUE);
		testCase("2 >= 1\n", TRUE);
		testCase("1 >= 2\n", FALSE);
		testNegativeCase("1 >= 1\n", FALSE);
		testNegativeCase("1 >= 2\n", TRUE);
	}
	
	@Test
	public void testLesser() throws Exception {
		testCase("1 < 2\n", TRUE);
		testCase("1 < 2 && 2 < 3\n", TRUE);
		testCase("2 < 1\n", FALSE);
		testCase("1 < 2 && 2 < 1\n", FALSE);
		testNegativeCase("1 < 2\n", FALSE);
		testNegativeCase("1 < 2 && 2 < 1\n", TRUE);
	}
	
	@Test
	public void testLesserEq() throws Exception {
		testCase("1 <= 1\n", TRUE);
		testCase("1 <= 2\n", TRUE);
		testCase("2 <= 1\n", FALSE);
		testNegativeCase("1 <= 1\n", FALSE);
	}
	
	@Test
	public void testMul() throws Exception {
		testCase("4*2\n", 8);
		testCase("4*2*2\n", 16);
		testNegativeCase("1*1\n", 0);
		testNegativeCase("1*1*0\n", 1);
	}
	
	@Test
	public void testNot() throws Exception {
		testCase("!false\n", TRUE);
		testCase("!true\n", FALSE);
		testCase("!(false)\n", TRUE);
		testNegativeCase("!false\n", FALSE);
		testNegativeCase("!true\n", TRUE);
	}
	
	@Test
	public void testNotEqual() throws Exception {
		testCase("1 != 2\n", TRUE);
		testCase("1 != 1\n", FALSE);
		testCase("true != false\n", TRUE);
		testCase("true != true\n", FALSE);
		testNegativeCase("1 != 2\n", FALSE);
		testNegativeCase("true != true\n", TRUE);
	}
	
	@Test
	public void testNum() throws Exception {
		testCase("1\n", 1);
		testCase("10\n", 10);
		testCase("-1\n", -1);
		testNegativeCase("1\n", 0);
	}
	
	@Test
	public void testOr() throws Exception {
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
		testCase("1-1\n", 0);
		testCase("1-2-3\n", -4);
		testNegativeCase("1-1\n", -1);
		testNegativeCase("1-1-1\n", 0);
	}
	
	@Test
	public void testDecl() throws Exception {
		testCase("decl x = 1 in x+1 end\n", 2);
		testCase("decl x = 1 in decl y = 2 in x+y end end\n", 3);
		testCase("decl x = decl y = 2 in 2*y end in x*3\n", 4);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y end\n", 5);
		testCase("decl x = 1 in x+2 end * decl y = 1 in 2*y+x end\n", 5);
		testNegativeCase("x+1", 0);
		testNegativeCase("decl x = 1 in x+1 end + x", 0);
	}
	
	@Test
	public void testMixed() throws Exception {
		testCase("1+2*3 > 1/1*1\n", TRUE);
		testCase("1+2*3+2/2\n", 8);
		testCase("true || false != false && false\n", TRUE);
		testCase("(20+20)/(4*5)\n", 2);
	}
	
}
