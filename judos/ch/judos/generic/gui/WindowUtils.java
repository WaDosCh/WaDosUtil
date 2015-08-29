package ch.judos.generic.gui;
import java.awt.Dimension;

import javax.swing.JFrame;

public class WindowUtils {
	public static void centerWindow(JFrame jframe) {
		Dimension screenSize = jframe.getToolkit().getScreenSize();
		jframe.setLocation(screenSize.width / 2 - jframe.getWidth() / 2, screenSize.height / 2
			- jframe.getHeight() / 2);

	}
}
