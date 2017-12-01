package environment;

public interface IEnvironment<T> {

	IEnvironment<T> beginScope();

	IEnvironment<T> endScope();

	void assoc(String id, T value) throws DuplicateIdentifierException;

	T find(String id) throws UndeclaredIdentifierException;

}