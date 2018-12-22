package ch.judos.generic.data.parse;

import ch.judos.generic.data.date.Time;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class TimeHMS extends ComposedFormat {

	private IntervallFormat hour;
	private IntervallFormat minute;
	private IntervallFormat second;

	/**
	 * 
	 */
	public TimeHMS() {
		this.hour = new IntervallFormat(0, 24);
		this.minute = new IntervallFormat(0, 59);
		this.second = new IntervallFormat(0, 59);
		init(this.hour, ":", this.minute, ":", this.second);
	}

	/**
	 * @return the parsed time
	 */
	@Override
	public Time getObject() {
		if (!this.set)
			return null;
		return new Time(this.hour.getI(), this.minute.getI(), this.second.getI());
	}

	/**
	 * @return the parsed time
	 */
	public Time getTime() {
		return getObject();
	}

}
