package ch.judos.generic.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * @since 23.09.2013
 * @author Julian Schelker
 */
/**
 * @since 23.09.2013
 * @author Julian Schelker
 */
public class GraphicsEx {
	public Graphics g;
	private int translatedX = 0;
	private int translatedY = 0;

	public GraphicsEx(Graphics g) {
		this.g = g;
	}

	public void drawStringCentered(String str, int x, int y) {
		FontMetrics fm = this.g.getFontMetrics();
		int w = fm.stringWidth(str);
		this.g.drawString(str, x - w / 2, y + fm.getAscent() / 2 - 1);
		// g.drawLine(x - 20, y, x + 20, y);
	}

	public void translate(int x, int y) {
		this.translatedX += x;
		this.translatedY += y;
		this.g.translate(x, y);
	}

	public void translateToOrigin() {
		this.g.translate(-this.translatedX, -this.translatedY);
		this.translatedX = 0;
		this.translatedY = 0;
	}

	/**
	 * Sets this graphics context's current color to the specified color. All
	 * subsequent graphics operations using this graphics context use this
	 * specified color.
	 * 
	 * @param color
	 *            - the new rendering color.
	 */
	public void setColor(Color color) {
		this.g.setColor(color);
	}

	/**
	 * Fills an oval bounded by the specified rectangle with the current color.
	 * 
	 * @param x
	 *            - the x coordinate of the upper left corner of the oval to be
	 *            filled.
	 * 
	 * @param y
	 *            - the y coordinate of the upper left corner of the oval to be
	 *            filled.
	 * 
	 * @param width
	 *            - the width of the oval to be filled.
	 * @param height
	 *            - the height of the oval to be filled.
	 */
	public void fillOval(int x, int y, int width, int height) {
		this.g.fillOval(x, y, width, height);
	}
}
