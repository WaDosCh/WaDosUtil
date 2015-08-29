package ch.judos.generic.games.unitCoordination;

/**
 * if a method is called and it is assumed that there is free space, this
 * exception terminates the method unusually
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public class NoFreeSpaceException extends RuntimeException {

	private static final long serialVersionUID = -8101964666375940569L;

	/**
	 * standard error message "No free space available"
	 */
	public NoFreeSpaceException() {
		super("No free space available");
	}

	/**
	 * @param msg
	 *            pass your own error message
	 */
	public NoFreeSpaceException(String msg) {
		super(msg);
	}
}
