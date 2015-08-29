package ch.judos.generic.data.parse;

import java.util.regex.Pattern;

/**
 * @since 14.01.2014
 * @author Julian Schelker
 */
public class WordFormat extends SimpleFormat {

	protected String data;

	public WordFormat() {
		super(Pattern.compile("(\\w+)(.*)"));
	}

	@Override
	public String matchAndConsume(String input) {
		if (doesntMatch(input))
			return null;
		this.data = this.matcher.group(1);
		return this.matcher.group(2);
	}

	public String getWord() {
		return this.data;
	}

	@Override
	protected Object getObject() {
		return this.data;
	}

}
