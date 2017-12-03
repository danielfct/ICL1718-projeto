package compiler;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import types.FunType;
import types.IType;
public interface ICodeBuilder {


	void dump(PrintStream out, String resultType) throws FileNotFoundException;

	StackFrame newFrame();
	
	void setCurrentFrame(StackFrame frame);

	StackFrame getCurrentFrame();

	Reference requestReference(IType type);

	Reference getReference(IType type);
	
	// TypeSignature requestSignature(FunType type);
	
	TypeSignature getSignature(FunType type);
	
	Closure newClosure(FunType type);

	String toJasmin(IType t);

	// pushes stack pointer onto the operand stack
	void loadSP();
	
	
	
	// Bytecode instructions

	// make a comment
	void emit_comment(String comment); 

	// make a new line
	void emit_newline();
	
	// create new object
	void emit_new(String classname); 

	// invoke instance method; special handling for superclass, private, and
	// instance initialization method invocations
	void emit_invokespecial(String classname, String methodname, String descriptor); 

	// invoke interface method
	void emit_invokeinterface(String interfacename, String methodname, String descriptor, int nArgs); 

	// push null into stack
	void emit_null(); 

	// getfield pops objectref (a reference to an object); from the stack,
	// retrieves the value of the field identified by <classname/fieldname>
	// from objectref, and pushes the one-word or two-word value onto the
	// operand stack
	// Examples:
	// getfield Frame_n1/SL LFrame_n;
	// getfield Frame_1/loc_X I
	void emit_getfield(String classname, String fieldname, String descriptor); 

	// putfield sets the value of the field identified by <classname/fieldname>
	// in objectref (a reference to an object);
	// to the single or double word value on the operand stack
	// Examples:
	// putfield Frame_n1/SL LFrame_n;
	// putfield Frame_1/loc_X I
	void emit_putfield(String classname, String fieldname, String descriptor); 

	// This pops the top single-word value off the operand stack, and then
	// pushes that value twice
	// - i.e. it makes an extra copy of the top item on the stack
	void emit_dup(); 

	// Pops objectref (a reference to an object or array); off the stack and
	// stores it in local variable <n>
	void emit_astore(int n); 

	// retrieves an object reference from a local variable and pushes it onto
	// the operand stack
	void emit_aload(int n); 

	// checks that the top item on the operand stack (a reference to an object
	// or array); can be cast to a given type
	void emit_checkcast(String t); 

	// push value into the stack
	void emit_push(int n); 

	// add the two top integer values on stack
	void emit_add(); 

	// multiple the two top integer values on stack
	void emit_mul(); 

	// divide the two top integer values on stack
	void emit_div(); 

	// subtract the two top integer values on stack
	void emit_sub(); 

	// xor between the two top integer values on stack
	void emit_xor(); 

	// push 0 or 1 into the stack
	void emit_bool(boolean val);

	// push a value from -1 to 5 into the stack
	void emit_val(int val);

	// compare if two integer values are equal, if so, jump to given label
	void emit_icmpeq(String label); 

	// compare if two integer values are not equal, if so, jump to given label
	void emit_icmpne(String label); 

	// compare if first integer value is greater or equal than second integer
	// value, if so, jump to given label
	void emit_icmpge(String label); 

	// compare if first integer value is greater than second integer value, if
	// so, jump to given label
	void emit_icmpgt(String label); 

	// compare if first integer value is lesser or equal than second integer
	// value, if so, jump to given label
	void emit_icmple(String label); 

	// compare if first ineger value is lesser than second integer value, if so,
	// jump to given label
	void emit_icmplt(String label); 

	// compare value on top of stack with 0, if equal, jumpo to given label
	void emit_ifeq(String label); 

	// check if value on top of stack is diferent than 0
	void emit_ifne(String label); 

	// jump to given label
	void emit_jump(String label); 

	// anchor the given label
	void emit_anchor(String label); 

	// bitwise int AND
	void emit_and(); 

	// bitwise int OR
	void emit_or(); 

	// pop the value on top of the stack
	void emit_pop(); 

	// swap the top 2 values on stack
	void emit_swap(); 

	// pushes the int value held in a local variable onto the operand stack
	void emit_iload(int position); 

}
