package ast;

import compiler.CodeBlock;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.TypingException;
import values.IValue;
import values.TypeMismatchException;

public class ASTIf implements ASTNode {

	final ASTNode condition;
	final ASTNode ifExpression;
	final ASTNode elseExpression;
	private IType type;
	
	public ASTIf(ASTNode condition, ASTNode ifExpression, ASTNode elseExpression ) {
		this.condition = condition;
		this.ifExpression = ifExpression;
		this.elseExpression = elseExpression;
		this.type = null;
	}
	
	@Override
	public String toString() {
		return "if " + condition + " then " + ifExpression + " else " + elseExpression + " end";
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(if(E1, E2, E3), env, m0) = [		(v1, m1) = eval(E1, env, m0);
//												if (v1 = T) then 
//													(v2, m2) = eval(E2, env, m1);
//												else 
//													(v2 , m2) = eval(E3, env, m1);
//												(v2 , m2) 
//										 ]
		return null;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(if(E1, E2, E3), env ) = [ 	t1 = typecheck(E1, env );
//												if (t1 != bool) then 
//													none
//												else [
//													  t2 = typecheck(E2, env);
//													  t3 = typecheck(E3, env);
//													  if (t2 == none) or (t3 == none) or (t2 != t3) then 
//															none
//													  else t2 
//												] 
//											]
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
