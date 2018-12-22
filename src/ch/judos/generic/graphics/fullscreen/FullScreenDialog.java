package ch.judos.generic.graphics.fullscreen;

/**
 * makes it easy to ask the user for a certain fullscreen resolution
 * 
 * @since 21.02.2013
 * @author Julian Schelker
 * @version 1.0 / 21.02.2013
 */
public abstract class FullScreenDialog {
	/**
	 * @param title
	 * @return the dialog
	 */
	public static FullScreenDialogI createDialog(String title) {
		return new FullScreenDialogComplex(title);
	}
}
