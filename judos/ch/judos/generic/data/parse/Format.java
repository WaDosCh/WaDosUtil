package ch.judos.generic.data.parse;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public abstract class Format {

	/**
	 * whether some data for get() has been stored
	 */
	protected boolean set = false;
	/**
	 */
	protected Object defaultValue = null;

	/**
	 * remove unnecessary objects and memory
	 */
	public abstract void cleanup();

	/**
	 * only use after matchAndConsume
	 * 
	 * @return returns the parsed input, or null if none matched yet
	 */
	public final Object get() {
		if (!this.set)
			return this.defaultValue;
		return getObject();
	}

	/**
	 * @param input
	 * @return null if input couldn't be matched, rest of string if it did
	 */
	public abstract String matchAndConsume(String input);

	/**
	 * @param input
	 * @return whether the input could be matches or not
	 */
	public boolean matches(String input) {
		boolean failed = false;
		String remaining = matchAndConsume(input);
		if (remaining == null)
			failed = true;

		cleanup();
		if (failed)
			removeData();
		return "".equals(remaining);
	}

	/**
	 * remove matched data
	 */
	public void removeData() {
		// todo: check implementation
	}

	/**
	 * sets a default value to return if nothing was matched
	 * 
	 * @param defaultValue
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the data parsed
	 */
	protected abstract Object getObject();

}
