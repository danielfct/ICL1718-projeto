package util;

public class DuplicateIdentifierException extends Exception {

	private String id;

	public DuplicateIdentifierException(String id) {
		this.setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
