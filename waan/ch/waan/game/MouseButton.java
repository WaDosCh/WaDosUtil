package ch.waan.game;

import java.awt.event.MouseEvent;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Constants for the 3 mouse buttons.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-22
 */
public enum MouseButton {

	/** The left mouse button */
	LEFT,
	/** The middle mouse button (mouse wheel button) */
	MIDDLE,
	/** The right mouse button */
	RIGHT;

	/**
	 * Returns the MouseButton for the button of a given MouseEvent
	 * 
	 * @param e
	 *            the event to get the button for
	 * @return the corresponding button
	 * @throws IllegalArgumentException
	 *             if the given MouseEvent does not hold a valid mouse button.
	 */
	public static @NonNull MouseButton getButtonForEvent(@NonNull MouseEvent e) {
		int btn = e.getButton();
		switch (btn) {
			case MouseEvent.BUTTON1 :
				return LEFT;
			case MouseEvent.BUTTON2 :
				return MIDDLE;
			case MouseEvent.BUTTON3 :
				return RIGHT;
			default :
				throw new IllegalArgumentException("mouse event " + e
					+ "had illegal button value: " + btn);
		}
	}
}
