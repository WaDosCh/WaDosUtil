package ch.judos.generic.graphics.fullscreen;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;

import ch.judos.generic.graphics.Drawable;

/**
 * supports opening a fullscreen-window or a window to draw content
 * 
 * @since 15.03.2013
 * @author Julian Schelker
 * @version 1.0 / 15.03.2013
 */
public class FullScreenOrWindowed extends FullScreen {
	/**
	 * initialize with a specific device/screen, resolution
	 * 
	 * @param device
	 * @param resolution
	 * @param content
	 * @param appTitle
	 */
	public FullScreenOrWindowed(GraphicsDevice device, DisplayMode resolution,
		Drawable content, String appTitle) {
		super(device, resolution, content, appTitle);
	}

	/**
	 * opens a window and draws the content inside this window
	 */
	public void startWindowedScreen() {
		openScreen(2, false, false);
	}

	/**
	 * opens a fake fullscreen, which doesn't show any window borders
	 */
	public void startWindowedUndecorated() {
		openScreen(2, false, true);
	}

	public Dimension getSize() {
		return new Dimension(this.mode.getWidth(), this.mode.getHeight());
	}

	public Frame getFrame() {
		return this.frame;
	}
}
