package ch.judos.generic.data.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * part of a declared format of a string (e.g. date = "dd.mm.yyyy")
 * 
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public abstract class SimpleFormat extends Format {

	/**
	 * the pattern to use for this formatPart
	 */
	protected Pattern pattern;

	/**
	 * matcher for the last input, used by doesntMatch()<br>
	 * is set to null after matching!
	 */
	protected Matcher matcher;

	/**
	 * @param p
	 *            the pattern to use for this format part
	 */
	public SimpleFormat(Pattern p) {
		this.pattern = p;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.Format#cleanup()
	 */
	@Override
	public void cleanup() {
		this.matcher = null;
	}

	/**
	 * matches as much of the input as possible and returns the rest
	 * 
	 * @param input
	 *            rest of the string
	 * @return null if string can't be matched
	 */
	@Override
	public abstract String matchAndConsume(String input);

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.Format#removeData()
	 */
	@Override
	public void removeData() {
		this.set = false;
	}

	/**
	 * @param input
	 * @return whether the given pattern matches the input
	 */
	protected boolean doesntMatch(String input) {
		this.matcher = this.pattern.matcher(input);
		this.set = this.matcher.matches();
		return !this.set;
	}

}
