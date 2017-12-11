package ast;

import compiler.ICodeBuilder;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.IType;
import types.TypingException;
import values.IValue;
import values.Suspension;
import values.TypeMismatchException;

public abstract class ASTNode implements IASTNode {

	@Override
	public abstract IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException;
	
	@Override
	public IValue force(IValue value) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		if (value instanceof Suspension)
			value = ((Suspension)value).evaluate();
		return value;
	}

	@Override
	public abstract IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException;

	@Override
	public abstract void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException;

	@Override
	public abstract IType getType();
	
	

}
