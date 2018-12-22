package ch.judos.generic.swing;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * creates a dialog with an entry in the taskbar
 * 
 * @since 28.02.2012
 * @author Julian Schelker
 * @version 1.01 / 02.03.2013
 * 
 */
public class JDialogWithTaskbarEntry extends JDialog {

	private static final long serialVersionUID = -8635486826953215847L;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		JDialogWithTaskbarEntry x = new JDialogWithTaskbarEntry("Test");
		x.setVisible(true);
		Thread.sleep(2500);
		x.setVisible(false);
	}

	/**
	 * @param title
	 */
	public JDialogWithTaskbarEntry(String title) {
		super(new DummyFrame(title));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			((DummyFrame) getParent()).dispose();
		}
	}

	static class DummyFrame extends JFrame {
		private static final long serialVersionUID = 6035300590783149332L;

		DummyFrame(String title) {
			super(title);
			setUndecorated(true);
			setVisible(true);
			setLocationRelativeTo(null);
		}
	}

}
