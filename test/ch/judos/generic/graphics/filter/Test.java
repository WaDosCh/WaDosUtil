package ch.judos.generic.graphics.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @since 13.09.2015
 * @author Julian Schelker
 */
public class Test {
	public static void main(String[] args) throws IOException {

		BufferedImage container = ImageIO.read(new File("container.png"));
		Color[] colors = new Color[]{new Color(255, 0, 0), new Color(128, 50, 50),
			new Color(0, 0, 255), new Color(50, 50, 128)};
		for (int i = 0; i < colors.length; i++) {
			ColorTintFilter x = new ColorTintFilter(colors[i], 0.75f);

			BufferedImage redContainer = x.filter(container, null);
			ImageIO.write(redContainer, "png", new File("Container-" + i + ".png"));
		}
	}
}
