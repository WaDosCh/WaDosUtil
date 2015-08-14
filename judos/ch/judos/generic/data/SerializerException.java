package ch.judos.generic.data;

/**
 * @since 26.04.2012
 * @author Julian Schelker
 * @version 1.01 22.02.2013
 */
public class SerializerException extends Exception {
	private static final long	serialVersionUID	= -5228333595544445373L;

	/**
	 * @param msg
	 * @param e
	 */
	public SerializerException(String msg, Exception e) {
		super(msg + ": " + e.getMessage());
		this.setStackTrace(e.getStackTrace());
	}

	/**
	 * @param msg
	 */
	public SerializerException(String msg) {
		super(msg);
	}
}
