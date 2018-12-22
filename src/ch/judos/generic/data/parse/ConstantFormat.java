package ch.judos.generic.data.parse;

import java.util.regex.Pattern;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class ConstantFormat extends SimpleFormat {

	/**
	 * string to expect
	 */
	protected String expect;

	/**
	 * @param expect
	 */
	public ConstantFormat(String expect) {
		super(Pattern.compile("(" + Pattern.quote(expect) + ")(.*)"));
		this.expect = expect;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.Format#getObject()
	 */
	@Override
	public Object getObject() {
		return this.expect;
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
		return this.matcher.group(2);
	}

}
