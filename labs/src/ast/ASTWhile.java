package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import memory.MemoryManagement;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class ASTWhile implements ASTNode {

	final ASTNode condition;
	final ASTNode doExpression;
	private IType type;
	
	public ASTWhile(ASTNode condition, ASTNode doExpression) {
		this.condition = condition;
		this.doExpression = doExpression;
		this.type = null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	@Override
	public Eval eval(IEnvironment<IValue> env, MemoryManagement mem) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(while(E1, E2), env, m0) = [		(v1, m1) = eval(E1, env, m0);
//												if (v1 = T) then [ 
//													(v2, m2) = eval(E2, env, m1);
//													(v, m1) = eval(while(E1, E2), m2) 
//												]
//												else (False , m1) 
//										 ]
		return null;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(while(E1, E2), env) = [ 		t1 = typecheck(E1, env);
//												if (t1 != bool) then 
//													none
//												else [ 
//														t2 = typecheck(E2, env);
//														if (t2 == none) then 
//															none
//														else bool 
//													  ] 
//										]
		return null;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// TODO Auto-generated method stub
	}

	@Override
	public IType getType() {
		return type;
	}

}
