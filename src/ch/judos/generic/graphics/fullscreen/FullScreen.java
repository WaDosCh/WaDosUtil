package ch.judos.generic.graphics.fullscreen;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;

import ch.judos.generic.graphics.Drawable;
import ch.judos.generic.graphics.MouseInfoI;
import ch.judos.generic.math.MathJS;

/**
 * use FullScreenOrWindows instead of this class!
 * 
 * @since 04.09.2011
 * @author Julian Schelker
 * @version 1.15 / 21.02.2013
 */
public class FullScreen implements MouseInfoI {
	/**
	 * @param device
	 * @param config
	 * @return true if device can show this displaymode config
	 */
	public static boolean deviceSupportsDisplayMode(GraphicsDevice device, DisplayMode config) {
		DisplayMode[] modes = device.getDisplayModes();
		for (DisplayMode m : modes) {
			if (m.equals(config))
				return true;
		}
		return false;
	}

	/**
	 * @param m
	 * @return w x h x color bit x image fps
	 */
	public static String displayModeToString(DisplayMode m) {
		return m.getWidth() + " x " + m.getHeight() + " x " + m.getBitDepth() + "bit  "
			+ m.getRefreshRate() + "fps";
	}

	/**
	 * @return available devices/ screens/ graphic cards
	 */
	public static GraphicsDevice[] getDevices() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	}

	/**
	 * @param device
	 * @return all possible displaymodes for device
	 */
	public static DisplayMode[] getDisplayModes(GraphicsDevice device) {
		return device.getDisplayModes();
	}

	/**
	 * @param device
	 * @return sorted array of the display modes - desc by width, height, bit
	 *         depth, fps
	 */
	public static DisplayMode[] getDisplayModesSorted(GraphicsDevice device) {
		List<DisplayMode> list = new ArrayList<>();
		for (DisplayMode m : device.getDisplayModes())
			list.add(m);
		Collections.sort(list, new FullScreen.DMComp());
		return list.toArray(new DisplayMode[]{});
	}

	/**
	 * @param device
	 * @return available graphic memory for this device
	 */
	public static String getMaxAcceleratedMemory(GraphicsDevice device) {
		double b = device.getAvailableAcceleratedMemory();
		StringBuffer result = new StringBuffer();
		if (b < 0) {
			result.append("ca. ");
			b = Math.abs(b);
		}
		String[] unit = {"Bytes", "KiB", "MiB", "GiB", "TiB", "PiB"};
		int u = 0;
		while (b >= 1024) {
			u++;
			b /= 1024;
		}
		// Remove dependency MathJS:
		// result.append(Math.round(b)+" "+unit[u]);
		result.append(MathJS.round(b, 2) + " " + unit[u]);
		return result.toString();
	}

	/**
	 * @param device
	 * @return largest possible resolution for this device
	 */
	public static DisplayMode getMaxResolution(GraphicsDevice device) {
		DisplayMode[] modes = device.getDisplayModes();
		int maxPixels = 0;
		int maxFps = 0;
		DisplayMode best = null;
		for (DisplayMode m : modes) {
			int pixels = m.getWidth() * m.getHeight();
			int fps = m.getRefreshRate();
			if (pixels > maxPixels) {
				maxPixels = pixels;
				best = m;
				maxFps = fps;
			}
			else if (pixels == maxPixels && fps > maxFps) {
				maxFps = fps;
				best = m;
			}
		}
		return best;
	}

	/**
	 * @param device
	 * @return dimension of resolution
	 */
	public static Dimension getMaxResolutionDimension(GraphicsDevice device) {
		DisplayMode m = getMaxResolution(device);
		return new Dimension(m.getWidth(), m.getHeight());
	}

	/**
	 * @param device
	 * @return w x h x color bit x image fps
	 */
	public static String getMaxResolutionString(GraphicsDevice device) {
		return displayModeToString(getMaxResolution(device));
	}

	/**
	 * whether the screen is cleared before drawing a frame
	 */
	protected boolean clear;
	/**
	 * frame inside which fullscreen content is displayed
	 */
	public Frame frame;
	/**
	 * deviced/ screen used for displaying
	 */
	protected GraphicsDevice device;
	/**
	 * resolution and frequency used
	 */
	protected DisplayMode mode;
	/**
	 * content of the view
	 */
	protected Drawable draw;
	/**
	 * title for the window
	 */
	protected String title;
	/**
	 * refresh timer
	 */
	protected Timer timer;
	/**
	 * prevents flickering
	 */
	protected BufferStrategy bufferStrategy;
	/**
	 * calc fps - last check ms
	 */
	protected long fps_ms;
	/**
	 * calc fps - current counter
	 */
	protected int fps_co;
	/**
	 * calculated fps
	 */
	public int FPS;
	private int individualRefreshRateFps;
	private boolean individualRefreshRate;
	private ArrayList<KeyListener> klists;
	private ArrayList<MouseListener> mlists;
	private ArrayList<WindowListener> wlists;
	private ArrayList<MouseMotionListener> mmlist;

	/**
	 * @param device
	 * @param config
	 * @param content
	 * @param appTitle
	 */
	public FullScreen(GraphicsDevice device, DisplayMode config, Drawable content,
		String appTitle) {
		if (!deviceSupportsDisplayMode(device, config))
			throw new InvalidParameterException("DisplayMode is not supported by this device.");
		this.device = device;
		this.mode = config;
		this.draw = content;
		this.title = appTitle;
		this.individualRefreshRate = false;
		this.klists = new ArrayList<>();
		this.mlists = new ArrayList<>();
		this.wlists = new ArrayList<>();
		this.mmlist = new ArrayList<>();
	}

	/**
	 * @param list
	 *            A KeyListener to add permanently (is saved and still active
	 *            after you exit and start FullScreen again)
	 */
	public void addKeyListener(KeyListener list) {
		this.klists.add(list);
		if (this.frame != null)
			this.frame.addKeyListener(list);
	}

	/**
	 * @param list
	 */
	public void addMouseListener(MouseListener list) {
		this.mlists.add(list);
		if (this.frame != null)
			this.frame.addMouseListener(list);
	}

	/**
	 * @param list
	 */
	public void addWindowListener(WindowListener list) {
		this.wlists.add(list);
		if (this.frame != null)
			this.frame.addWindowListener(list);
	}

	/**
	 * closes the fullscreen view
	 */
	public void dispose() {
		endScreen();
	}

	/**
	 * closes the fullscreen view
	 */
	public void endScreen() {
		this.timer.cancel();
		this.frame.dispose();
		this.device.setFullScreenWindow(null);
	}

	/**
	 * open up the fullscreen view with certain number of buffers
	 * 
	 * @param numBuffers
	 *            number of buffers to predraw on before displaying
	 * @param fullscreen
	 *            whether the content should be drawn in fullscreen mode
	 * @param undecorated
	 *            for window mode: if true will not show border of window
	 */
	public void openScreen(int numBuffers, boolean fullscreen, boolean undecorated) {
		this.frame = new Frame(this.title, this.device.getDefaultConfiguration());
		this.frame.setResizable(false);
		this.frame.setUndecorated(fullscreen);
		this.frame.setIgnoreRepaint(true);
		for (KeyListener k : this.klists)
			this.frame.addKeyListener(k);
		for (MouseListener m : this.mlists)
			this.frame.addMouseListener(m);
		for (WindowListener w : this.wlists)
			this.frame.addWindowListener(w);
		for (MouseMotionListener m : this.mmlist)
			this.frame.addMouseMotionListener(m);
		this.frame.setBackground(Color.black);
		if (fullscreen)
			this.device.setFullScreenWindow(this.frame);
		else {
			this.device.setFullScreenWindow(null);
			this.frame.setUndecorated(undecorated);
			this.frame.setVisible(true);
			this.frame.setSize(this.mode.getWidth(), this.mode.getHeight());
		}
		this.frame.createBufferStrategy(numBuffers);
		if (fullscreen) {
			if (this.device.isDisplayChangeSupported())
				this.device.setDisplayMode(this.mode);
			else
				System.out.println("Could not change displaymode. Instead running in "
					+ this.device.getDisplayMode());
		}
		this.fps_ms = System.currentTimeMillis();
		this.fps_co = 0;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					drawIt();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.timer = new Timer();
		int delay = 1000 / this.mode.getRefreshRate();
		if (this.individualRefreshRate)
			delay = 1000 / this.individualRefreshRateFps;
		this.timer.scheduleAtFixedRate(task, 0, delay);
		this.bufferStrategy = this.frame.getBufferStrategy();
	}

	/**
	 * @param list
	 */
	public void removeKeyListener(KeyListener list) {
		this.klists.remove(list);
		if (this.frame != null)
			this.frame.removeKeyListener(list);
	}

	/**
	 * @param list
	 */
	public void removeMouseListener(MouseListener list) {
		this.mlists.remove(list);
		if (this.frame != null)
			this.frame.removeMouseListener(list);
	}

	/**
	 * @param list
	 */
	public void removeWindowListener(WindowListener list) {
		this.wlists.remove(list);
		if (this.frame != null)
			this.frame.removeWindowListener(list);
	}

	/**
	 * @param value
	 *            turn on or off the clearing before drawing each frame
	 */
	public void setClearing(boolean value) {
		this.clear = value;
	}

	/**
	 * @param fps
	 *            If content needs to be refreshed less than displayMode
	 *            suggests
	 */
	public void setIndividualRefreshRate(int fps) {
		this.individualRefreshRate = true;
		this.individualRefreshRateFps = fps;
	}

	/**
	 * open up the fullscreen view
	 */
	public void startFullScreen() {
		openScreen(2, true, false);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.title + "  running in " + this.mode.getWidth() + "x"
			+ this.mode.getHeight() + " " + this.mode.getBitDepth() + "bit "
			+ this.mode.getRefreshRate() + "fps";
	}

	/**
	 * called when the view is drawn
	 */
	protected void drawIt() {
		Graphics gs = this.bufferStrategy.getDrawGraphics();
		if (!this.bufferStrategy.contentsLost()) {
			gs.clearRect(0, 0, this.mode.getWidth(), this.mode.getHeight());
			this.draw.paint(gs);
			this.fps_co++;
			if (System.currentTimeMillis() - this.fps_ms >= 1000) {
				this.FPS = this.fps_co;
				this.fps_co = 0;
				this.fps_ms = System.currentTimeMillis();
			}
			gs.dispose();
			this.bufferStrategy.show();
		}
	}

	protected static class DMComp implements Comparator<DisplayMode> {
		/**
		 * (non-Javadoc)
		 * 
		 * @param o1
		 * @param o2
		 * @return comparision value
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(DisplayMode o1, DisplayMode o2) {
			int a1 = o2.getWidth() - o1.getWidth();
			if (a1 != 0)
				return a1;
			a1 = o2.getHeight() - o1.getHeight();
			if (a1 != 0)
				return a1;
			a1 = o2.getBitDepth() - o1.getBitDepth();
			if (a1 != 0)
				return a1;
			a1 = o2.getRefreshRate() - o1.getRefreshRate();
			return a1;
		}
	}

	public DisplayMode getDisplayMode() {
		return this.mode;
	}

	public void addMouseMotionListener(MouseMotionListener motion) {
		this.mmlist.add(motion);
		if (this.frame != null)
			this.frame.addMouseMotionListener(motion);
	}

	public void removeMouseMotionListener(MouseMotionListener motion) {
		this.mmlist.remove(motion);
		if (this.frame != null)
			this.frame.removeMouseMotionListener(motion);
	}

	@Override
	public Point getMousePosition() {
		if (this.frame == null)
			return null;
		return this.frame.getMousePosition();
	}
}
