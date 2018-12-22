package ch.judos.generic.graphics.drawing;

import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * @since 15.09.2015
 * @author Julian Schelker
 */
public class TexturePaintJS {
	private static HashMap<BufferedImage, TexturePaint> cache = new HashMap<BufferedImage, TexturePaint>();

	public static TexturePaint getPaintForImage(BufferedImage image) {
		if (cache.containsKey(image))
			return cache.get(image);
		TexturePaint paint = new TexturePaint(image, new Rectangle(0, 0, image.getWidth(),
			image.getHeight()));
		cache.put(image, paint);
		return paint;
	}
}
