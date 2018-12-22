package ch.judos.generic.graphics.fullscreen;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

/**
 * interface for fullscreen dialogs
 * 
 * @since 21.02.2013
 * @author Julian Schelker
 * @version 1.0 / 21.02.2013
 */
public interface FullScreenDialogI {

	/**
	 * @return the display mode (resolution, color depth, fps)
	 */
	public DisplayMode getDisplayMode();

	/**
	 * @return the device (screen)
	 */
	public GraphicsDevice getGraphicsDevice();

	/**
	 * @return whether the user hit cancel
	 */
	public boolean isCanceled();

}
