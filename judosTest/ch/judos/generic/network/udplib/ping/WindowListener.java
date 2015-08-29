package ch.judos.generic.network.udplib.ping;

import java.awt.event.WindowEvent;

public class WindowListener implements java.awt.event.WindowListener {

	private CheckInternet c;

	public WindowListener(CheckInternet checkInternet) {
		this.c = checkInternet;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.c.close();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.c.close();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// do nothing
	}

}
