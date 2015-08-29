package ch.judos.generic.math;

import java.math.BigDecimal;
import java.util.HashSet;

/**
 * @since 02.03.2012
 * @author Julian Schelker
 * @version 1.01 / 02.03.2012
 * 
 */
public class ConvertNumber {

	/**
	 * @param nr
	 *            number
	 * @param maxPrecision
	 *            max digits after comma
	 * @return the number converted to binary
	 */
	public static String decToBin(String nr, int maxPrecision) {
		BigDecimal b = new BigDecimal(nr);
		StringBuffer output = new StringBuffer();
		HashSet<BigDecimal> checkRepeat = new HashSet<>();
		for (int i = 0; true; i++) {
			checkRepeat.add(b);
			b = b.multiply(new BigDecimal(2));
			if (b.compareTo(BigDecimal.ONE) >= 0) {
				b = b.subtract(BigDecimal.ONE);
				output.append(1);
			}
			else
				output.append(0);
			if (b.compareTo(BigDecimal.ZERO) == 0)
				break;
			if (checkRepeat.contains(b)) {
				output.append("_");
				break;
			}
			if (i >= maxPrecision) {
				output.append("...");
				break;
			}
		}
		return output.toString();
	}

	/**
	 * @param i
	 *            some integer
	 * @return the equivalent byte if integer is unsigned and in range
	 */
	public static byte int2UnsignedByte(int i) {
		return (byte) i;
	}

	/**
	 * bad performence if string is mostly not an number
	 * 
	 * @param str
	 * @return true if the string is a double
	 */
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * bad performence if string is mostly not an number
	 * 
	 * @param str
	 * @return true if the string is an int
	 */
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * @param b
	 *            look at the byte as an unsigned byte
	 * @return the equivalent int value
	 */
	public static int unsignedByte2Int(byte b) {
		return Byte.toUnsignedInt(b);
	}
}
