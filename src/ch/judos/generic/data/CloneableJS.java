package ch.judos.generic.data;

/**
 * enforces the implementation of the public method clone.
 * 
 * @since 28.04.2012
 * @author Julian Schelker
 * @version 1.0 / 23.02.2013
 */
public interface CloneableJS extends Cloneable {

	/**
	 * @return the cloned object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException;

}
