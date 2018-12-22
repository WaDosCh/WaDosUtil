package ch.judos.generic.data.format;

import ch.judos.generic.math.MathJS;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class ByteData {

	public static String autoFormat(double bytes) {
		return autoFormat(bytes, 1);
	}

	public static String autoFormat(double bytes, int decimalCiffers) {
		return autoFormat(bytes, decimalCiffers, false);
	}

	public static String autoFormat(double bytes, int decimalCiffers, boolean forceDecimals) {
		int u = 0;
		String[] units = new String[]{"Bytes", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB",
			"YiB"};
		while (bytes >= 1024) {
			bytes /= 1024;
			u++;
		}
		bytes = MathJS.round(bytes, decimalCiffers);
		String r;
		if (!forceDecimals) {
			if (bytes == (long) bytes)
				r = String.format("%d", (long) bytes);
			else
				r = String.format("%s", bytes);
		}
		else {
			r = String.format("%." + decimalCiffers + "f", bytes);
		}
		return r + " " + units[u];
	}

	public static void main(String[] args) {
		// size rounded to 2 decimal ciffers
		System.out.println(autoFormat(1200, 2));
		// size rounded to 2 decimal ciffers, but trailing zeros are truncated
		System.out.println(autoFormat(1024 * 1024, 2));
		// force all decimal ciffers
		System.out.println(autoFormat(1024 * 1024, 2, true));

		// usage of negative decimal places
		System.out.println(autoFormat(1024 * 57, 0, false));
		System.out.println(autoFormat(1024 * 57, -1, false));
	}
}
