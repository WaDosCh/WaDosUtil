package ch.judos.generic.data.parse;

import ch.judos.generic.data.date.Time;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class TimeHM extends ComposedFormat {

	private IntervallFormat hour;
	private IntervallFormat minute;

	/**
	 * 
	 */
	public TimeHM() {
		this.hour = new IntervallFormat(0, 24);
		this.minute = new IntervallFormat(0, 59);
		init(this.hour, ":", this.minute);
	}

	/**
	 * @return the parsed time
	 */
	@Override
	public Time getObject() {
		if (!this.set)
			return null;
		return new Time(this.hour.getI(), this.minute.getI());
	}

	/**
	 * @return the parsed time
	 */
	public Time getTime() {
		return getObject();
	}

}
