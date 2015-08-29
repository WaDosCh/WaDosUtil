package ch.judos.generic.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the common behavior of an object which can have an "audience".<br>
 * if an object can change its state and should only communicate with listeners
 * it can extend this class. Use the methods available to add listeners or
 * remove them.<br>
 * If the object extending this class changes it's state it can use the method
 * notifyListeners to notify all available listeners.
 * 
 * @since 11.10.2011
 * @author Julian Schelker
 * @version 1.11 / 22.02.2013
 */
public abstract class Listenable {

	// you'll not forget to call the constructor, since it is not needed :)
	/**
	 * the list of listeners for the object
	 */
	protected List<Listener> listener = new ArrayList<>();

	/**
	 * @param l
	 *            the listener you want to add
	 */
	public void addActionListener(Listener l) {
		this.listener.add(l);
	}

	/**
	 * @param l
	 *            the listener you want to remove
	 */
	public void removeActionListener(Listener l) {
		this.listener.remove(l);
	}

	/**
	 * call this method if you want to inform all listeners about an occured
	 * event
	 * 
	 * @param message
	 * @param value
	 */
	protected void notifyListeners(String message, Object value) {
		for (Listener l : this.listener)
			l.actionPerformed(this, message, value);
	}

}
