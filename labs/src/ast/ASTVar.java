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

public class ASTVar implements ASTNode {

	final ASTNode value;
	private IType type;
	
	public ASTVar(ASTNode v) {
		this.value = v;
		this.type = null;
	}
	
	
	@Override
	public String toString() {
		return "var(" + value + ")";
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		eval(var(E), env, m0) = [ 	(v1 , m1) = eval(E, env, m0);
//										(ref, m2) = m1.new(v1);
//										(ref, m2) 
//								   ]
		return null;
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
//		typecheck(var(E), env ) = [ 	t = typecheck( E, env ); 
//										ref{t}
//								   ]
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
