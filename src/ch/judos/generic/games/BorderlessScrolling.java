package ch.judos.generic.games;

import java.awt.*;

import javax.swing.SwingUtilities;

import ch.judos.generic.math.MathJS;

/**
 * @since 31.01.2015
 * @author Julian Schelker
 */
public abstract class BorderlessScrolling {

	/**
	 * coordinates of left top corner in the terrain (at start =0)<br>
	 * if x increases, you are scrolling to the left since the terrain is moving
	 * to the right on the screen
	 */
	protected Component comp;
	protected float locX;
	protected float locY;
	protected Dimension viewPort;
	protected Rectangle scrollIn;

	public static final int SCROLL_OUTSIDE_PX = 10;

	public BorderlessScrolling(Dimension viewPort) {
		super();
		this.viewPort = viewPort;
		this.scrollIn = new Rectangle(-SCROLL_OUTSIDE_PX, -SCROLL_OUTSIDE_PX, viewPort.width
			+ 2 * SCROLL_OUTSIDE_PX, viewPort.height + 2 * SCROLL_OUTSIDE_PX);
	}

	/**
	 * subtract this from any object coordinate you want to draw on the map<br>
	 * 
	 * @return the coordinates of the left,top terrain corner on the screen
	 */
	public Point getPosition() {
		return new Point((int) this.locX, (int) this.locY);
	}

	/**
	 * @param c
	 *            the component relative to which the mouse coordinates are
	 *            retrieved
	 */
	public void setRelativeTo(Component c) {
		this.comp = c;
	}

	protected Point getMousePoint() {
		Point point = MouseInfo.getPointerInfo().getLocation();
		if (this.comp != null)
			SwingUtilities.convertPointFromScreen(point, this.comp);
		return point;
	}

	/**
	 * updates the scrolling<br>
	 */
	public void update() {
		Point point = getMousePoint();
		if (!this.scrollIn.contains(point))
			return;

		scrollH(point, this.viewPort);
		scrollV(point, this.viewPort);
	}

	protected float checkScrolling(float value, float border) {
		float diff = border - value;
		int direction = (int) Math.signum(diff);
		float change = MathJS.min(Math.abs(diff), getScrollSpeedPerFrame());
		return value + direction * change;
	}

	protected void scrollLeft() {
		this.locX = checkScrolling(this.locX, Float.NEGATIVE_INFINITY);
	}

	protected void scrollRight() {
		this.locX = checkScrolling(this.locX, Float.POSITIVE_INFINITY);
	}

	void scrollH(Point point, Dimension size) {
		// Left scrolling
		if (point.x < getScrollBorder())
			scrollLeft();
		// Right scrolling
		if (point.x > size.width - getScrollBorder())
			scrollRight();
	}

	protected void scrollUp() {
		this.locY = checkScrolling(this.locY, Float.NEGATIVE_INFINITY);
	}

	void scrollV(Point point, Dimension size) {
		// Up scrolling
		if (point.y < getScrollBorder())
			scrollUp();
		// Down scrolling
		if (point.y > size.height - getScrollBorder())
			scrollDown();
	}

	protected void scrollDown() {
		this.locY = checkScrolling(this.locY, Float.POSITIVE_INFINITY);
	}

	/**
	 * This is needed because the scrolling speed should be independent of the
	 * update speed
	 * 
	 * @return how many frames are drawn in your game
	 */
	protected abstract int getFps();

	/**
	 * @return size of border area, in which the mouse has to be to scroll
	 */
	protected abstract int getScrollBorder();

	/**
	 * @return how many pixels should be scrolled in one second
	 */
	protected abstract int getScrollSpeed();

	/**
	 * @return how many pixels are scrolled in one frame
	 */
	protected float getScrollSpeedPerFrame() {
		return (float) getScrollSpeed() / getFps();
	}

}