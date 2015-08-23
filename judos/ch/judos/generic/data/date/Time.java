package ch.judos.generic.data.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.judos.generic.data.parse.TimeFormat;

/**
 * @since 07.03.2012
 * @author Julian Schelker
 * @version 1.01 / 22.02.2013
 */
public class Time {

	// some identifier and helper variables
	private static GregorianCalendar	c	= new GregorianCalendar();

	/**
	 * @return a list of all possible formating characters
	 */
	public static ArrayList<String> getFormatChars() {
		String formats = "aABghGHis";
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < formats.length(); i++)
			result.add(formats.substring(i, i + 1));
		return result;
	}

	/**
	 * tries to parse the given line to following formats:<br>
	 * hour:minute<br>
	 * hour:minute:seconds
	 * 
	 * @param line
	 *            the time as string
	 * @return null if the time could not be parsed
	 */
	public static Time parse(String line) {
		TimeFormat t = new TimeFormat();
		if (t.matches(line))
			return t.getTime();
		return null;
	}

	/**
	 * the hour of this time
	 */
	protected int	hour;
	/**
	 * the minute of this time
	 */
	protected int	minute;

	/**
	 * the second of this time
	 */
	protected int	second;

	/**
	 * the date will save the current hour, minute and second
	 */
	public Time() {
		c.setTime(new java.util.Date());
		this.hour = c.get(Calendar.HOUR_OF_DAY);
		this.minute = c.get(Calendar.MINUTE);
		this.second = c.get(Calendar.SECOND);
	}

	/**
	 * see also the static parse method for parsing time
	 * 
	 * @param hour
	 *            give some values to initialize this date
	 * @param minute
	 */
	public Time(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
		this.second = 0;
	}

	/**
	 * see also the static parse method for parsing time
	 * 
	 * @param hour
	 *            give some values to initialize this date
	 * @param minute
	 * @param second
	 */
	public Time(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	/**
	 * @return new time representing the same hour, minute and second
	 */
	@Override
	public Time clone() {
		Time t = new Time();
		t.hour = this.hour;
		t.minute = this.minute;
		t.second = this.second;
		return t;
	}

	/**
	 * @param time
	 *            same other time object
	 * @return 1 if this.isAfter(time) <br>
	 *         -1 if this.isBefore(time) <br>
	 *         0 if the two times represent the same hour, minute and second
	 */
	public int compareTo(Time time) {
		if (isAfter(time))
			return 1;
		else if (isBefore(time))
			return -1;
		return 0;
	}

	/**
	 * @param hour
	 * @param minute
	 * @param second
	 * @return true if this represents the given time
	 */
	@SuppressWarnings("all")
	public boolean equals(int hour, int minute, int second) {
		return this.hour == hour && this.minute == minute && this.second == second;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.hour;
		result = prime * result + this.minute;
		result = prime * result + this.second;
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		if (this.hour != other.hour)
			return false;
		if (this.minute != other.minute)
			return false;
		if (this.second != other.second)
			return false;
		return true;
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return this.hour;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return this.minute;
	}

	/**
	 * @return the second
	 */
	public int getSecond() {
		return this.second;
	}

	/**
	 * @param time
	 *            another time
	 * @return true if this time is in the futur relatively to the given time
	 */
	public boolean isAfter(Time time) {
		return time.isBefore(this);
	}

	/**
	 * @param time
	 *            another time
	 * @return true if this time is in the past relatively to the given time
	 */
	public boolean isBefore(Time time) {
		if (this.hour < time.hour)
			return true;
		if (this.hour > time.hour)
			return false;
		if (this.minute < time.minute)
			return true;
		if (this.minute > time.minute)
			return false;
		if (this.second < time.second)
			return true;
		if (this.second > time.second)
			return false;
		return false;
	}

	/**
	 * @return true if time is valid
	 */
	public boolean isValid() {
		return this.hour >= 0 && this.hour < 24 && this.minute >= 0 && this.minute < 60
			&& this.second >= 0 && this.second <= 60;
	}

	/**
	 * @return toString("H:i:s")
	 */
	@Override
	public String toString() {
		return toString("H:i:s");
	}

	/**
	 * Like the php Function date (format follows the same standards, but only
	 * for times) See: <a href="http://php.net/manual/de/function.date.php">PHP
	 * date</a>
	 * 
	 * @param format
	 *            : one char for every format, use / as escape char
	 * @return the formated string
	 */
	public String toString(String format) {
		StringBuffer r = new StringBuffer("");
		int anzahlZeichen = format.length();
		for (int pos = 0; pos < anzahlZeichen; pos++) {
			String nextChar = format.substring(pos, pos + 1);
			boolean x = formatChar(nextChar, r);
			if (!x) {
				if (nextChar.equals("\\") || nextChar.equals("/")) {
					pos++;
					nextChar = format.substring(pos, pos + 1);
					r.append(nextChar);
				}
				else {
					r.append(nextChar);
				}
			}
		}
		return r.toString();
	}

	/**
	 * @param form
	 * @param r
	 * @return true if format was found, false otherwise
	 */
	protected boolean formatChar(String form, StringBuffer r) {
		if (form.equals("a")) {
			if (this.hour < 12)
				r.append("am");
			else
				r.append("pm");
		}
		else if (form.equals("A")) {
			if (this.hour < 12)
				r.append("AM");
			else
				r.append("PM");
		}
		else if (form.equals("B")) {
			int sec = this.hour * 3600 + this.minute * 60 + this.second;
			int swatchTime = (int) (sec / 86.4);
			r.append(swatchTime);
		}
		else if (form.equals("g"))
			r.append((this.hour + 12) % 12);
		else if (form.equals("h")) {
			int h = (this.hour + 12) % 12;
			if (h < 10)
				r.append(0);
			r.append(h);
		}
		else if (form.equals("G"))
			r.append(this.hour);
		else if (form.equals("H")) {
			if (this.hour < 10)
				r.append(0);
			r.append(this.hour);
		}
		else if (form.equals("i")) {
			if (this.minute < 10)
				r.append(0);
			r.append(this.minute);
		}
		else if (form.equals("s")) {
			if (this.second < 10)
				r.append(0);
			r.append(this.second);
		}
		else
			return false;
		return true;
	}
}
