package ast;

import java.util.ArrayList;

import compiler.CodeBlock;
import types.IType;
import types.TypingException;
import util.DuplicateIdentifierException;
import util.IEnvironment;
import util.UndeclaredIdentifierException;
import values.IValue;
import values.TypeMismatchException;

public class ASTDecl implements ASTNode {
	
	static class Declaration {
		
		String id;
		ASTNode def;
		
		public Declaration(String id, ASTNode def) {
			this.id = id;
			this.def = def;
		}
		
	}
	
	ArrayList<Declaration> declarations; //TODO List
	ASTNode body;
	
	public ASTDecl() {
		declarations = new ArrayList<>();
	}
	
	public void addBody(ASTNode body) {
		this.body = body;
	}
	
	public void newBinding(String id, ASTNode e) {
		declarations.add(new Declaration(id, e));
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IValue> newenv = env.beginScope();	
		for (Declaration d : declarations)
			newenv.assoc(d.id, d.def.eval(env));	
		IValue value = body.eval(newenv);	
		env.endScope();
		
		return value;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, UndeclaredIdentifierException, DuplicateIdentifierException {
		IEnvironment<IType> newenv = env.beginScope();
		for (Declaration d: declarations)
			newenv.assoc(d.id, d.def.typecheck(env));
		IType type = body.typecheck(newenv);
		env.endScope();
		
		return type;	
	}

	@Override
	public void compile(CodeBlock code) { // <<< IEnvironment<Integer> env
		
		// Create and initialize the stackframe (frame_i) in the compiler
		// Use code to manage stackframes ( code.newFrame() ??)
		// Generate code that initializes the stack frame
		/*
		new frame_id
		dup
		invokespecial frame_id/<init>()V
		*/
		
		// initialize SL in the stackframe
		/*
		dup
		aload SP
		putfield frame_id/SL Lframe_up; 
		*/
		
		// beginScope
		// For each declaration:
			// assoc id_i to loc_i (fresh)
			// add loc_i to frame (use the type)
			// compile its expression and generate code that stores the value
			// in the original environment
			/*
			  dup
			  [[ E_i ]]
			  putfield frame_id/loc_i type;
			*/
		// terminate by storing the stack pointer
		/*
		astore SP
		*/
		
		// For the main declaration body
		// compiles it, in an environment that "knows" that loc_i corresponds to id_i
		/*
		[[ E ]] 
		*/
		
		// this corresponds to the endScope
		/*
		aload SP
		checkcast frame_id
		getfield frame_id/SL Lframe_up;
		astore SP
		 */
		
		
	}
}