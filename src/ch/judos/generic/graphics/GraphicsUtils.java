package ch.judos.generic.graphics;

import java.awt.AlphaComposite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @since 20.09.2014
 * @author Julian Schelker
 */
public interface GraphicsUtils {

	Graphics2D getGraphics();

	public default void drawStringCentered(String str, int x, int y) {
		Graphics2D g = getGraphics();
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(str);
		g.drawString(str, x - w / 2, y + fm.getAscent() / 2 - 1);
	}

	public default void setGraphicsAlpha(double alpha) {
		AlphaComposite ac =
			java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
		getGraphics().setComposite(ac);
	}

	/**
	 * draws the image at the graphics origin
	 */
	public default void drawCentered(BufferedImage image) {
		getGraphics().drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);
	}
}
