package ch.judos.generic.data.parse;

import java.util.regex.Pattern;

/**
 * parses a number
 * 
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class NumberFormat extends SimpleFormat {

	/**
	 * parsed data
	 */
	protected int data;

	/**
	 * 
	 */
	public NumberFormat() {
		super(Pattern.compile("(\\d+)(.*)"));
		this.data = 0;
	}

	/**
	 * @return the data as int
	 */
	public int getI() {
		if (!this.set)
			throw new RuntimeException("getI() can only be called if data is set in FP");
		return Integer.valueOf(get().toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.SimpleFormat#getObject()
	 */
	@Override
	public Object getObject() {
		return this.data;
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
		return this.matcher.group(2);
	}

}
