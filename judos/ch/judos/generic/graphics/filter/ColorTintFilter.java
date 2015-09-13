package ch.judos.generic.graphics.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ch.judos.generic.graphics.ImageUtils;

/**
 * @since 13.09.2015
 * @author Julian Schelker
 */
public class ColorTintFilter extends AbstractFilter {
	private final Color mixColor;
	private final float mixValue;

	/**
	 * @param mixColor
	 * @param mixValue
	 *            for games/building coloring a mixValue of 0.75 is quite good
	 */
	public ColorTintFilter(Color mixColor, float mixValue) {
		if (mixColor == null) {
			throw new IllegalArgumentException("mixColor cannot be null");
		}

		this.mixColor = mixColor;
		if (mixValue < 0.0f) {
			mixValue = 0.0f;
		}
		else if (mixValue > 1.0f) {
			mixValue = 1.0f;
		}
		this.mixValue = mixValue;
	}

	public float getMixValue() {
		return this.mixValue;
	}

	public Color getMixColor() {
		return this.mixColor;
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dst) {
		if (dst == null) {
			dst = createCompatibleDestImage(src, null);
		}

		int width = src.getWidth();
		int height = src.getHeight();

		int[] pixels = new int[width * height];
		ImageUtils.getPixels(src, 0, 0, width, height, pixels);
		mixColor(pixels);
		ImageUtils.setPixels(dst, 0, 0, width, height, pixels);
		return dst;
	}

	private void mixColor(int[] inPixels) {
		float[] mixHsb = Color.RGBtoHSB(this.mixColor.getRed(), this.mixColor.getGreen(),
			this.mixColor.getBlue(), null);
		for (int i = 0; i < inPixels.length; i++) {
			inPixels[i] = mixPixel(inPixels[i], mixHsb);
		}
	}

	private int mixPixel(int argb, float[] mixHsb) {
		int a = argb & 0xFF000000;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = (argb) & 0xFF;
		float[] hsb = Color.RGBtoHSB(r, g, b, null);

		r = (int) (r * (1f - this.mixValue) + this.mixColor.getRed() * this.mixValue);
		g = (int) (g * (1f - this.mixValue) + this.mixColor.getGreen() * this.mixValue);
		b = (int) (b * (1f - this.mixValue) + this.mixColor.getBlue() * this.mixValue);
		float[] hsbNew = Color.RGBtoHSB(r, g, b, null);
		hsbNew[2] = hsb[2] * (1f - this.mixValue) + hsb[2] * mixHsb[2] * this.mixValue;
		hsbNew[1] = hsb[1] * (1f - this.mixValue) + hsbNew[1] * this.mixValue;
		int rgb = Color.HSBtoRGB(hsbNew[0], hsbNew[1], hsbNew[2]);
		return a | (rgb & 0xFFFFFF);
	}
}