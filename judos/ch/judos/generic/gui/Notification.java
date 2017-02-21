package ch.judos.generic.gui;

import javax.swing.JOptionPane;

/**
 * @since 14.09.2011
 * @author Julian Schelker
 * @version 1.01 / 10.01.2012
 */
public class Notification {
	/**
	 * returned if yes is clicked
	 */
	public static int PROCEED_YES = 0;
	/**
	 * returned if no is clicked
	 */
	public static int PROCEED_NO = 1;
	/**
	 * returned if cancel is clicked
	 */
	public static int PROCEED_CANCEL = 2;

	public static int PROCEED_CLOSE = -1;

	/**
	 * @param title
	 * @param message
	 * @return the option that was clicked, see public fields
	 */
	public static int proceed(String title, String message) {
		String[] options = {"Ja", "Nein", "Abbrechen!"};
		return proceed(title, message, options);
	}

	/**
	 * @param title
	 * @param message
	 * @param options
	 * @return the option that was clicked, index from the provided options
	 */
	public static int proceed(String title, String message, String[] options) {
		return proceed(title, message, options, 0);
	}

	/**
	 * @param title
	 * @param message
	 * @param options
	 * @param selected
	 * @return the option that was clicked, index from the provided options
	 */
	public static int proceed(String title, String message, String[] options, int selected) {
		return JOptionPane.showOptionDialog(null, message, title,
			JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
			options[selected]);
	}

	/**
	 * shows a simple error message
	 * 
	 * @param title
	 * @param message
	 */
	public static void notifyErr(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * shows a simple notification message
	 * 
	 * @param title
	 * @param message
	 */
	public static void notifyInfo(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

}
