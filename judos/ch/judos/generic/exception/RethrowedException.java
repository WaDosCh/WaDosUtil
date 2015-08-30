package ch.judos.generic.exception;

/**
 * @since 23.08.2015
 * @author Julian Schelker
 */
public class RethrowedException extends RuntimeException {

	private Exception exception;

	private static final long serialVersionUID = -1547776241581998916L;

	public RethrowedException(Exception e) {
		super(e.getMessage());
		this.exception = e;
		this.setStackTrace(e.getStackTrace());
	}

	public Exception getEncapsulatedException() {
		return this.exception;
	}

	@Override
	public String toString() {
		return super.toString() + " (encapsulated: " + this.exception + ")";
	}
}
