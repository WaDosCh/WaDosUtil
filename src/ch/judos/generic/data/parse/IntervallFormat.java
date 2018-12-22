package ch.judos.generic.data.parse;

import java.util.regex.Pattern;

/**
 * only matches numbers in a given intervall
 * 
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class IntervallFormat extends SimpleFormat {

	/**
	 * @param number
	 * @return the number of digits of the number
	 */
	private static int digits(int number) {
		return String.valueOf(number).length();
	}

	private int min;
	private int max;

	private int data;

	/**
	 * @param min
	 * @param max
	 */
	public IntervallFormat(int min, int max) {
		super(Pattern.compile("(\\d{" + digits(min) + "," + digits(max) + "})(.*)"));
		this.min = min;
		this.max = max;
	}

	/**
	 * @return the data as int
	 */
	public int getI() {
		if (!this.set)
			throw new RuntimeException("getI() can only be called if data is set in FP");
		Object value = get();
		if (value instanceof Integer)
			return (Integer) value;
		return Integer.valueOf((String) get());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.SimpleFormat#matchAndConsume(java.lang.String)
	 */
	@Override
	public String matchAndConsume(String input) {
		if (doesntMatch(input))
			return null;
		this.data = Integer.parseInt(this.matcher.group(1));
		if (this.data >= this.min && this.data <= this.max)
			return this.matcher.group(2);
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.SimpleFormat#getObject()
	 */
	@Override
	protected Object getObject() {
		return this.data;
	}

}
