package util;

public class DuplicateIdentifierException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public final String id;

	public DuplicateIdentifierException(String id) {
		super(id);
		this.id = id;
	}

}
