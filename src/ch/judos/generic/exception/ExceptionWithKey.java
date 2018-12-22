package ch.judos.generic.exception;

public class ExceptionWithKey extends RuntimeException {

	private String key;
	private static final long serialVersionUID = -5259684850770436520L;

	public ExceptionWithKey(String key, String message) {
		super(message);
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public String toString() {
		return "ExceptionWithKey: " + this.key + " (" + this.getMessage() + ")";
	}
}
