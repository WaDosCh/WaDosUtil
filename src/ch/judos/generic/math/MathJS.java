package ch.judos.generic.math;

import java.util.List;

/**
 * @since 04.09.2011
 * @author Julian Schelker
 */
public class MathJS {

	/**
	 * @param list
	 * @return the standard deviation of all values in the list<br>
	 *         calculated as follow: <br>
	 *         stdv= sum( ( x_i - mean)^2 )/ (n-1)
	 */
	public static double stdv(List<? extends Number> list) {
		double mean = sum(list) / list.size();
		double sum = list.stream().mapToDouble(x -> Math.pow(x.doubleValue() - mean, 2)).sum();
		return sum / (list.size() - 1);

	}

	/**
	 * @param list
	 * @return the sum of all values in the list
	 */
	public static double sum(List<? extends Number> list) {
		double sum = 0;
		for (Number t : list) {
			sum += t.doubleValue();
		}
		return sum;
	}

	public static int ceil(double number) {
		return (int) Math.ceil(number);
	}

	public static int floor(double number) {
		return (int) Math.floor(number);
	}

	/**
	 * @param number
	 * @param min
	 * @param max
	 * @return the number clamped to the invervall. if the number is smaller than min then min is
	 *         returned, if the number if bigger than max then max is returned. otherwise the number
	 *         itself is returned.
	 */
	public static double clamp(double number, double min, double max) {
		if (number < min)
			return min;
		if (number > max)
			return max;
		return number;
	}

	/**
	 * @param base base of the logarithm
	 * @param number some number
	 * @return log_a(b)
	 */
	public static double log(double base, double number) {
		return Math.log(number) / Math.log(base);
	}

	/**
	 * @param number
	 * @param divisor
	 * @return number / divisor and rounded to the next greater integer
	 */
	public static int divideAndCeil(int number, int divisor) {
		int result = number / divisor;
		if (number % divisor > 0)
			result++;
		return result;
	}

	/**
	 * returns the total cross of a number.
	 * 
	 * @param number
	 * @param base the radix of the number, for decimal numbers: 10, for binary numbers: 2
	 * @return the total cross
	 */
	public static int crossSum(int number, int base) {
		int rest = number;
		int sum = 0;
		while (rest > 0) {
			sum += rest % base;
			rest /= base;
		}
		return sum;
	}

	/**
	 * rounds a number such that the given precision is reached<br>
	 * example: roundToPrecision(1.234, 0.2) = 1.2
	 * 
	 * @param number
	 * @param precision
	 * @return the rounded number
	 */
	public static double roundToPrecision(double number, double precision) {
		return Math.round(number / precision) * precision;
	}

	/**
	 * @param number to round
	 * @param decimals digits after comma
	 * @return the rounded value
	 */
	public static double round(double number, int decimals) {
		double f = Math.pow(10, decimals);
		return Math.round(number * f) / f;
	}

	/**
	 * @param dx
	 * @param dy
	 * @return the length of the hypotenuse
	 */
	public static double dist2(double dx, double dy) {
		return Math.hypot(dx, dy);
	}

	/**
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return sqrt(x^2 + y^2 + z^2), the length of the hypotenuse in 3D
	 */
	public static double dist3(double dx, double dy, double dz) {
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
	}

	/**
	 * @param value to round
	 * @param decimals digits after comma
	 * @return the rounded value as string
	 */
	public static String roundToStr(double value, int decimals) {
		return String.valueOf(round(value, decimals));
	}

	/**
	 * @param a number one
	 * @param b number two
	 * @return if a==b returns 1, if a!=0 &amp;&amp; b==0 returns NaN, otherwise a/b
	 */
	public static double divideByZero(double a, double b) {
		if (b == 0) {
			if (a == 0)
				return 1;
			return Double.NaN;
		}
		return a / b;
	}

	/**
	 * @param arr values
	 * @return the maximum value of the array
	 */
	public static double max(double... arr) {
		double m = arr[0];
		for (int i = 1; i < arr.length; i++)
			if (arr[i] > m)
				m = arr[i];
		return m;
	}

	public static Number max(List<? extends Number> list) {
		double v = Double.MIN_VALUE;
		for (Number n : list) {
			v = Math.max(n.doubleValue(), v);
		}
		return v;
	}

	public static Number min(List<? extends Number> list) {
		double v = Double.MAX_VALUE;
		for (Number n : list) {
			v = Math.min(n.doubleValue(), v);
		}
		return v;
	}

	/**
	 * @param arr values
	 * @return the maximum value of the array
	 */
	public static float max(float... arr) {
		float m = arr[0];
		for (int i = 1; i < arr.length; i++)
			if (arr[i] > m)
				m = arr[i];
		return m;
	}

	/**
	 * @param arr values
	 * @return the minimum value of the array
	 */
	public static double min(double... arr) {
		double m = arr[0];
		for (int i = 1; i < arr.length; i++)
			if (arr[i] < m)
				m = arr[i];
		return m;
	}

	public static int minIndex(double... arr) {
		double m = arr[0];
		int index = 0;
		for (int i = 1; i < arr.length; i++)
			if (arr[i] < m) {
				m = arr[i];
				index = i;
			}
		return index;
	}

	public static int minIndex(float... arr) {
		float m = arr[0];
		int index = 0;
		for (int i = 1; i < arr.length; i++)
			if (arr[i] < m) {
				m = arr[i];
				index = i;
			}
		return index;
	}

	public static int maxIndex(float... arr) {
		float m = arr[0];
		int index = 0;
		for (int i = 1; i < arr.length; i++)
			if (arr[i] > m) {
				m = arr[i];
				index = i;
			}
		return index;
	}

	public static int maxIndex(double... arr) {
		double m = arr[0];
		int index = 0;
		for (int i = 1; i < arr.length; i++)
			if (arr[i] > m) {
				m = arr[i];
				index = i;
			}
		return index;
	}

	/**
	 * @param arr values
	 * @return the minimum value of the array
	 */
	public static float min(float... arr) {
		float m = arr[0];
		for (int i = 1; i < arr.length; i++)
			if (arr[i] < m)
				m = arr[i];
		return m;
	}

}
