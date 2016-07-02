package ch.judos.generic.graphics;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @since 20.09.2014
 * @author Julian Schelker
 */
public class GraphicsUtils {
	public static void setGraphicsAlpha(Graphics2D g, double alpha) {
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
			(float) alpha);
		g.setComposite(ac);
	}

	/**
	 * draws the image at the graphics origin
	 */
	public static void drawCentered(Graphics g, BufferedImage image) {
		g.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);
	}
}
