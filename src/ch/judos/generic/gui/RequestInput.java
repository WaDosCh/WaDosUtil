package ch.judos.generic.gui;

import javax.swing.JOptionPane;

/**
 * @since 10.02.2014
 * @author Julian Schelker
 */
public class RequestInput {

	public static String getString(String displayText) {
		return JOptionPane.showInputDialog(displayText);
	}

}
