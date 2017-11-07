package util;

import ast.ASTId.Address;

public interface ICompilationEnvironment extends IEnvironment<Integer> {
	
	CompilationEnvironment beginScope();

	CompilationEnvironment endScope();
	
	Address lookup(String id) throws UndeclaredIdentifierException;

}
