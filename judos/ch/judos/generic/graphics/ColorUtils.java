package ch.judos.generic.graphics;

import java.awt.Color;

/**
 * @since 09.02.2015
 * @author Julian Schelker
 */
public class ColorUtils {
	public static Color mix(Color c1, Color c2) {
		int red = (c1.getRed() + c2.getRed()) / 2;
		int green = (c1.getGreen() + c2.getGreen()) / 2;
		int blue = (c1.getBlue() + c2.getBlue()) / 2;
		int alpha = (c1.getAlpha() + c2.getAlpha()) / 2;
		return new Color(red, green, blue, alpha);
	}

	/**
	 * @param argb
	 *            integer color value
	 * @return red,green,blue in an array
	 */
	public static int[] argbToInt3(int argb) {
		int[] r = new int[3];
		r[0] = (argb >> 16) & 0xFF;
		r[1] = (argb >> 8) & 0xFF;
		r[2] = argb & 0xFF;
		return r;
	}

	/**
	 * @param bgColor
	 * @return nice string. e.g.: [r=255,g=80,b=1]
	 */
	public static String toString(Color color) {
		return "[r=" + color.getRed() + ",g=" + color.getGreen() + ",b=" + color.getBlue()
			+ "]";
	}
}
