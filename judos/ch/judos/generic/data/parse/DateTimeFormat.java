package ch.judos.generic.data.parse;

import ch.judos.generic.data.date.Date;
import ch.judos.generic.data.date.Time;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class DateTimeFormat extends ChoiceFormat {

	private DateFormat d;
	private TimeFormat t;

	/**
	 */
	public DateTimeFormat() {
		this.d = new DateFormat();
		this.t = new TimeFormat();

		Format d1 = new ComposedFormat(this.d, " ", this.t);
		Format d2 = new ComposedFormat(this.t, " ", this.d);

		init(d1, d2);
	}

	/**
	 * @return the parsed date
	 */
	public Date getDate() {
		return this.d.getObject();
	}

	/**
	 * @return the parsed time
	 */
	public Time getTime() {
		return this.t.getTime();
	}
}
