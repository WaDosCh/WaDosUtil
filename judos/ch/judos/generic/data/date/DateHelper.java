package ch.judos.generic.data.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.judos.generic.data.parse.DateFormat;

/**
 * static helper methods to handle dates
 * 
 * @since 01.07.2013
 * @author Julian Schelker
 * @version 1.0 / 01.07.2013
 */
public class DateHelper {
	// some identifier and helper variables
	private static GregorianCalendar c = new GregorianCalendar();

	/**
	 * Assumes that Calendar.Sunday = 1, Calendar.Monday = 2 and so on... names
	 * of the week days
	 */
	public static String[] weekdayNames = new String[]{"Sonntag", "Montag", "Dienstag",
		"Mittwoch", "Donnerstag", "Freitag", "Samstag"};
	/**
	 * names of the months
	 */
	public static String[] monthNames = new String[]{"Januar", "Februar", "M�rz", "April",
		"Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};

	/**
	 * @param year
	 * @param month
	 * @return the number of days the given month in the given year has
	 */
	public static int daysInMonth(int year, int month) {
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return all possible characters for formatting a string out of this date
	 */
	public static ArrayList<String> getFormatChars() {
		String formats = "dDjlNSwzWFmMntLYy";
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < formats.length(); i++)
			result.add(formats.substring(i, i + 1));
		return result;
	}

	/**
	 * @return current day [1-31]
	 */
	public static int getTodayDay() {
		c.setTime(new java.util.Date());
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return current month nr [1-12]
	 */
	public static int getTodayMonth() {
		c.setTime(new java.util.Date());
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * @return current year 4 digits (e.g.2013)
	 */
	public static int getTodayYear() {
		c.setTime(new java.util.Date());
		return c.get(Calendar.YEAR);
	}

	/**
	 * @param year
	 * @return true if the given year is a leap year
	 */
	public static boolean isLeapYear(int year) {
		return c.isLeapYear(year);
	}

	/**
	 * tries to parse the given line to following formats:<br>
	 * day.month.year<br>
	 * year-month-day<br>
	 * month/day/year
	 * 
	 * @param line
	 *            the date as string
	 * @return null if the date could not be parsed
	 */
	public static Date parse(String line) {
		DateFormat d = new DateFormat();
		if (d.matches(line))
			return d.getObject();
		return null;
	}

}
