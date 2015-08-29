package ch.judos.generic.data.parse;

import ch.judos.generic.data.date.Date;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class DateFormat extends ChoiceFormat {

	private IntervallFormat day;
	private IntervallFormat month;
	private NumberFormat year;

	/**
	 */
	public DateFormat() {
		this.day = new IntervallFormat(1, 31);
		this.month = new IntervallFormat(1, 12);
		this.year = new NumberFormat();

		Format d1 = new ComposedFormat(this.day, ".", this.month, ".", this.year);
		Format d2 = new ComposedFormat(this.year, "-", this.month, "-", this.day);
		Format d3 = new ComposedFormat(this.month, "/", this.day, "/", this.year);

		init(d1, d2, d3);
	}

	/**
	 * @return the parsed time
	 */
	@Override
	public Date getObject() {
		return new Date(this.year.getI(), this.month.getI(), this.day.getI());
	}
}
