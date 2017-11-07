package util;

public class UndeclaredIdentifierException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public final String id;
	
	public UndeclaredIdentifierException(String id) {
		super(id);
		this.id = id;
	}
	
}