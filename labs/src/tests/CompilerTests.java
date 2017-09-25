package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import main.Compiler;
import parser.ParseException;

public class CompilerTests {

	// This test function was designed to work with Unix like systems. 
	
	private void testCase(String expression, String result) 
			throws IOException, InterruptedException, ParseException, FileNotFoundException {
		
		Process p;
		
		p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "rm *.j *.class"});
	    p.waitFor();	    

	    System.out.println("Compiling to Jasmin source code");

	    Compiler.compile(expression);
		
	    System.out.println("Compiling to Jasmin bytecode");
	    
		p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "java -jar jasmin.jar *.j"});
	    p.waitFor();	    
	    assertTrue("Compiled to Jasmin bytecode", p.exitValue() == 0);

	    BufferedReader reader = 
		         new BufferedReader(new InputStreamReader(p.getInputStream()));

	    StringBuffer output = new StringBuffer();
        String line = "";			
        while ((line = reader.readLine())!= null) {
        		output.append(line + "\n");
        }
	    System.out.println(output.toString());

		p = Runtime.getRuntime().exec(new String[] {"sh","-c", "java Demo"});
	    p.waitFor();

	    reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    output = new StringBuffer();
        line = "";			
        while ((line = reader.readLine())!= null) {
        		output.append(line + "\n");
        }
	    System.out.println("Output: #"+output.toString()+"#");
	    
	    assertTrue(result.equals(output.toString()));
	}
	
	private void testCase(String expression, int value) throws FileNotFoundException, IOException, InterruptedException, ParseException {
		testCase(expression, value+"\n");		
	}

	@Test
	public void BasicTest() throws IOException, InterruptedException, ParseException{
		testCase("1\n", "1\n");
	}

	@Test
	public void testsLabClass02() throws Exception {
		testCase("1+2\n",3);
		testCase("1-2-3\n",-4);
		testCase("4*2\n",8);
		testCase("4/2/2\n",1);
	}
}





