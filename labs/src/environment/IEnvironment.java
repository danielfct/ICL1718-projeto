package environment;

public interface IEnvironment<T> {

	IEnvironment<T> beginScope();

	IEnvironment<T> endScope();

	void assoc(String id, T value) throws DuplicateIdentifierException;
	
	IEnvironment<T> update(String id, T value) throws UndeclaredIdentifierException;

	T find(String id) throws UndeclaredIdentifierException;

}