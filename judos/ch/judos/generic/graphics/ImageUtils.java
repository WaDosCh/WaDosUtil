package ch.judos.generic.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * some easy functions for images
 * 
 * @since 05.09.2011
 * @author Julian Schelker
 * @version 1.2 / 02.06.2013
 */

public class ImageUtils {

	/**
	 * @param image
	 * @param size
	 * @return
	 * @see #fitInto(Image, int, int)
	 */
	public static Image fitInto(Image image, Dimension size) {
		return fitInto(image, size.width, size.height);
	}

	/**
	 * @param image
	 * @param width
	 *            the maximum width for the rescaled image
	 * @param height
	 *            the maximum height for the rescaled image
	 * @return rescaled image to fit into the defined width & height. Aspect
	 *         ratio isn't changed
	 */
	public static Image fitInto(Image image, int width, int height) {
		int imgwidth = image.getWidth(null);
		int imgheight = image.getHeight(null);

		float ratio_is = (float) imgwidth / imgheight;
		float ratio_should = (float) width / height;

		if (ratio_is >= ratio_should) {
			return image.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		}
		return image.getScaledInstance(-1, height, Image.SCALE_SMOOTH);

	}

	/**
	 * @param image
	 * @param size
	 * @return
	 * @see #fitOnto(Image,int,int)
	 */
	public static Image fitOnto(Image image, Dimension size) {
		return fitOnto(image, size.width, size.height);
	}

	/**
	 * @param image
	 * @param width
	 *            the minimal width for the rescaled image
	 * @param height
	 *            the minimal height for the rescaled image
	 * @return rescaled image to fit onto the defined width & height. Aspect
	 *         ratio is preserved
	 */
	public static Image fitOnto(Image image, int width, int height) {
		int imgwidth = image.getWidth(null);
		int imgheight = image.getHeight(null);

		float ratio_is = (float) imgwidth / imgheight;
		float ratio_should = (float) width / height;

		if (ratio_is >= ratio_should) {
			return image.getScaledInstance(-1, height, Image.SCALE_SMOOTH);
		}
		// else {
		return image.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		// }

	}

	/**
	 * @param file
	 *            reference to the file
	 * @return the loaded bufferedimage - null if it could not be loaded
	 */
	public static BufferedImage loadBufferedImage(File file) {
		try {
			return ImageIO.read(file);
		}
		catch (IOException e) {
			return null;
		}
	}

	/**
	 * @param file
	 *            path of the file
	 * @return the loaded bufferedimage - null if it could not be loaded
	 */
	public static BufferedImage loadBufferedImage(String file) {
		return loadBufferedImage(new File(file));
	}

	/**
	 * @param name
	 * @return the loaded image - may not be fully loaded when the object is
	 *         returned
	 */
	public static Image loadImage(String name) {
		return Toolkit.getDefaultToolkit().getImage(name);
	}

	public static BufferedImage toBufferedImage(Image src) {
		if (src instanceof BufferedImage)
			return (BufferedImage) src;
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB; // other options
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}

}