package ch.judos.generic.data.date;

import java.util.concurrent.TimeUnit;

public class DateTime {

	public Date date;
	public Time time;

	public DateTime() {
		this.date = new Date();
		this.time = new Time();
	}

	@Override
	public String toString() {
		return this.date.toString() + " " + this.time.toString();
	}

	public long getTime() {
		return this.date.getTime() + this.time.getTime();
	}

	public long getDiffTo(DateTime otherDateTime, TimeUnit unit) {
		long diffInMS = otherDateTime.getTime() - this.getTime();
		return unit.convert(diffInMS, TimeUnit.MILLISECONDS);
	}
}
