package ch.judos.generic.data.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @since ??.??.2011
 * @author Julian Schelker
 * @version 1.11 / 22.02.2013
 */
public class Date implements Cloneable, Comparable<Date> {

	/**
	 * day
	 */
	protected int day;
	/**
	 * month
	 */
	protected int month;
	/**
	 * year
	 */
	protected int year;

	/**
	 * the date will save the current day, month and year
	 */
	public Date() {
		this.year = DateHelper.getTodayYear();
		this.month = DateHelper.getTodayMonth();
		this.day = DateHelper.getTodayDay();
	}

	/**
	 * see also the static parse method for parsing dates
	 * 
	 * @param year
	 *            give some values to initialize this date
	 * @param month
	 * @param day
	 */
	public Date(int year, int month, int day) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	/**
	 * Returns new date representing the same day, month, year
	 * 
	 * @return cloned object
	 */
	@Override
	public Date clone() {
		Date d = new Date();
		d.year = this.year;
		d.month = this.month;
		d.day = this.day;
		return d;
	}

	/**
	 * @param date
	 * @return 1 if this.isAfter(date) <br>
	 *         -1 if this.isBefore(date) <br>
	 *         0 if the two dates represent the same day, month and year
	 */
	@Override
	public int compareTo(Date date) {
		if (isAfter(date))
			return 1;
		else if (isBefore(date))
			return -1;
		return 0;
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @return true if this represents the given date
	 */
	@SuppressWarnings("all")
	public boolean equals(int year, int month, int day) {
		return this.year == year && this.month == month && this.day == day;
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
		result = prime * result + this.day;
		result = prime * result + this.month;
		result = prime * result + this.year;
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
		Date other = (Date) obj;
		if (this.day != other.day)
			return false;
		if (this.month != other.month)
			return false;
		if (this.year != other.year)
			return false;
		return true;
	}

	/**
	 * @param date
	 *            another date
	 * @return true if this date is in the futur relatively to the given date
	 */
	public boolean isAfter(Date date) {
		return date.isBefore(this);
	}

	/**
	 * @param date
	 *            another date
	 * @return true if this date is in the past relatively to the given date
	 */
	public boolean isBefore(Date date) {
		if (this.year < date.year)
			return true;
		if (this.year > date.year)
			return false;
		if (this.month < date.month)
			return true;
		if (this.month > date.month)
			return false;
		if (this.day < date.day)
			return true;
		if (this.day > date.day)
			return false;
		return false;
	}

	/**
	 * @return true if date is valid
	 */
	public boolean isValid() {
		return this.day > 0 && this.day <= DateHelper.daysInMonth(this.year, this.month)
			&& this.month > 0 && this.month < 13;
	}

	/**
	 * @return the date a day after this date
	 */
	public Date nextDay() {
		Date date = clone();
		date.day++;
		if (date.day > DateHelper.daysInMonth(date.year, date.month)) {
			date.day = 1;
			date.month++;
			if (date.month > 12) {
				date.month = 1;
				date.year++;
			}
		}
		return date;
	}

	/**
	 * @return the date a month after this date
	 */
	public Date nextMonth() {
		Date date = clone();
		date.month++;
		if (date.month > 12) {
			date.month = 1;
			date.year++;
		}
		int x = DateHelper.daysInMonth(date.year, date.month);
		if (date.day > x)
			date.day = x;
		return date;
	}

	/**
	 * @return the date a day before this date
	 */
	public Date prevDay() {
		Date date = clone();
		date.day--;
		if (date.day < 1) {
			date.month--;
			if (date.month < 1) {
				date.month = 12;
				date.year--;
			}
			date.day = DateHelper.daysInMonth(date.year, date.month);
		}
		return date;
	}

	/**
	 * @return the date a month before this date
	 */
	public Date prevMonth() {
		Date date = clone();
		date.month--;
		if (date.month < 1) {
			date.month = 12;
			date.year--;
		}
		int x = DateHelper.daysInMonth(date.year, date.month);
		if (date.day > x)
			date.day = x;
		return date;
	}

	/**
	 * @return toString("d.m.Y")
	 */
	@Override
	public String toString() {
		return toString("d.m.Y");
	}

	/**
	 * Like the php Function date (format follows the same standards, but only
	 * for dates) See: <a href="http://php.net/manual/de/function.date.php">PHP
	 * date</a>
	 * 
	 * @param format
	 *            : one char for every format, use / as escape char
	 * @return the formated string
	 */
	public String toString(String format) {
		GregorianCalendar c = new GregorianCalendar();
		c.set(this.year, this.month, this.day);
		StringBuffer r = new StringBuffer("");

		int anzahlZeichen = format.length();
		for (int pos = 0; pos < anzahlZeichen; pos++) {
			String cf = format.substring(pos, pos + 1);
			boolean found = formatChar(cf, r, c);
			if (!found) {
				if (cf.equals("\\") || cf.equals("/")) {
					pos++;
					cf = format.substring(pos, pos + 1);
					r.append(cf);
				}
				else
					r.append(cf);
			}
		}
		return r.toString();
	}

	/**
	 * adds the specified format to the string
	 * 
	 * @param cf
	 *            format
	 * @param r
	 *            stringbuffer to add the result
	 * @param c
	 *            the calender
	 * @return true if format is found, false otherwise
	 */
	protected boolean formatChar(String cf, StringBuffer r, GregorianCalendar c) {
		int weekdayNr = c.get(Calendar.DAY_OF_WEEK);
		// See assumes of the att weekdayNames
		String weekdayName = DateHelper.weekdayNames[weekdayNr - 1];
		if (cf.equals("d")) {
			if (this.day < 10)
				r.append("0");
			r.append(this.day);
		}
		else if (cf.equals("D"))
			r.append(weekdayName).substring(0, 3);
		else if (cf.equals("j"))
			r.append(this.day);
		else if (cf.equals("l"))
			r.append(weekdayName);
		else if (cf.equals("N")) {
			if (weekdayNr == 0)
				r.append(7);
			else
				r.append(weekdayNr);
		}
		else if (cf.equals("S")) {
			String[] postfix = new String[]{"st", "nd", "rd", "th"};
			if (this.day < 4)
				r.append(postfix[this.day - 1]);
			else
				r.append(postfix[3]);
		}
		else if (cf.equals("w"))
			r.append(weekdayNr);
		else if (cf.equals("z")) {
			int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
			r.append(dayOfYear - 1);
		}
		else if (cf.equals("W")) {
			int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
			r.append(weekOfYear);
		}
		else if (cf.equals("F"))
			r.append(DateHelper.monthNames[this.month - 1]);
		else if (cf.equals("m")) {
			if (this.month < 10)
				r.append(0);
			r.append(this.month);
		}
		else if (cf.equals("M"))
			r.append(DateHelper.monthNames[this.month - 1].substring(0, 3));
		else if (cf.equals("n"))
			r.append(this.month);
		else if (cf.equals("t")) {
			int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			r.append(daysInMonth);
		}
		else if (cf.equals("L")) {
			int leapYear = (c.isLeapYear(this.year) ? 1 : 0);
			r.append(leapYear);
		}
		else if (cf.equals("Y")) {
			if (this.year < 1000) {
				r.append(0);
				if (this.year < 100)
					r.append(0);
				if (this.year < 10)
					r.append(0);
			}
			r.append(this.year);
		}
		else if (cf.equals("y"))
			r.append(String.valueOf(this.year).substring(2));
		else
			return false;
		return true;
	}

}
