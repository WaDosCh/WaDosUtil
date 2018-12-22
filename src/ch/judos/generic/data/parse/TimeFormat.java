package ch.judos.generic.data.parse;

import ch.judos.generic.data.date.Time;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class TimeFormat extends ChoiceFormat {

	private TimeHM t1;
	private TimeHMS t2;

	/**
	 */
	public TimeFormat() {
		this.t1 = new TimeHM();
		this.t2 = new TimeHMS();
		init(this.t1, this.t2);
	}

	/**
	 * @return the parsed time
	 */
	public Time getTime() {
		return (Time) get();
	}
}
