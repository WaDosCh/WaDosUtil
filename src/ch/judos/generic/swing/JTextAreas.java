package ch.judos.generic.swing;

import java.awt.KeyboardFocusManager;

import javax.swing.JTextArea;

/**
 * @since ??.??.2012
 * @author Julian Schelker
 * @version 1.0 / 02.03.2013
 */
public class JTextAreas {

	/**
	 * set the tab character to follow tab change policy
	 * 
	 * @param component
	 */
	public static void setTabChangesFocus(JTextArea component) {
		component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		component.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
	}
}
