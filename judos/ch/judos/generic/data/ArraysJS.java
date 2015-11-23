package ch.judos.generic.data;

import java.util.function.Function;

/**
 * @since 05.11.2011
 * @author Julian Schelker
 * @version 1.02 / 23.02.2013
 */
public class ArraysJS {

	public static enum LoopResult {
		CONTINUE, BREAK;
	}

	public static <T> void foreach(T[] arr, Function<T, LoopResult> lambda) {
		for (int i = 0; i < arr.length; i++) {
			LoopResult l = lambda.apply(arr[i]);
			if (l == LoopResult.BREAK)
				break;
		}
	}

	/**
	 * @param arr
	 * @param obj
	 * @return i where arr[i].equals(obj)<br>
	 *         -1 if the object is not found
	 */
	public static int indexOf(Object[] arr, Object obj) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(obj))
				return i;
		return -1;
	}

	public static boolean contains(int[] arr, int value) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == value)
				return true;
		return false;
	}

	/**
	 * @param arr
	 * @param value
	 * @return whether arr holds the value
	 */
	public static boolean contains(Object[] arr, Object value) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(value))
				return true;
		return false;
	}

	/**
	 * extracts an array of the second dimension from the given array
	 * 
	 * @param arr
	 * @param index
	 *            which x coordinate to use
	 * @return the extracted array
	 */
	public static Object[] extractArr(Object[][] arr, int index) {
		Object[] result = new Object[arr.length];
		for (int i = 0; i < arr.length; i++)
			result[i] = arr[i][index];
		return result;
	}

	/**
	 * @param arr
	 * @return the same array which exchanged indices
	 */
	public static Object[][] shiftDim(Object[][] arr) {
		Object[][] result = new Object[arr[0].length][arr.length];
		for (int x = 0; x < arr.length; x++)
			for (int y = 0; y < arr[0].length; y++)
				result[x][y] = arr[y][x];
		return result;
	}

	/**
	 * the first entry will be exchanged with the last one, the second with the
	 * second last one and so one...
	 * 
	 * @param arr
	 */
	public static void swapEntries(float[] arr) {
		for (int i = 0; i < arr.length / 2; i++) {
			float x = arr[i];
			int backPos = arr.length - 1 - i;
			arr[i] = arr[backPos];
			arr[backPos] = x;
		}
	}

	/**
	 * the first entry will be exchanged with the last one, the second with the
	 * second last one and so one...
	 * 
	 * @param arr
	 */
	public static void swapEntries(Object[] arr) {
		for (int i = 0; i < arr.length / 2; i++) {
			Object x = arr[i];
			int backPos = arr.length - 1 - i;
			arr[i] = arr[backPos];
			arr[backPos] = x;
		}
	}

}
