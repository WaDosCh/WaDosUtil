package ch.wados.math;

/**
 * Collection of mathematical utility functions
 * 
 * @author Andreas Wälchli
 */
public final class Math {

	/**
	 * private constructor to hide constructor. Not actually usable!
	 * 
	 * @throws AssertionError
	 *             always
	 */
	private Math() {
		throw new AssertionError("unreachable");
	}

	/**
	 * Normalises an angle in radians into the range [0,2π). For NaN-inputs the
	 * input is returned.
	 * 
	 * @param angle
	 *            the raw angle in radians
	 * @return the normalised angle
	 */
	public static double normalizedAngle(double angle) {
		final double twopi = 2 * java.lang.Math.PI;
		if (Double.isNaN(angle))
			return angle;
		while (angle >= twopi)
			angle -= twopi;
		while (angle < 0)
			angle += twopi;
		return angle;
	}
}
