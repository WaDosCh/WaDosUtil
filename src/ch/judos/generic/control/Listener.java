package ch.judos.generic.control;

/**
 * A more abstract variant of an ActionListener.<br>
 * Use this if you need more parameters for your events occuring.
 * 
 * 
 * @since 11.10.2011
 * @author Julian Schelker
 * @version 1.11 / 22.02.2013
 */
public interface Listener {

	/**
	 * @param source
	 *            the source object
	 * @param message
	 *            an identifier for the action that happened
	 * @param value
	 *            some value to the action that happened
	 */
	public void actionPerformed(Object source, String message, Object value);
}
