package ch.judos.generic.data.parse;

import ch.judos.generic.data.date.Time;
import junit.framework.TestCase;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class FormatTests extends TestCase {

	/**
	 */
	public void testChoice() {
		TimeFormat t = new TimeFormat();
		assertTrue(t.matches("02:11:33"));
		Time tParsed = t.getTime();
		assertEquals(2, tParsed.getHour());
		assertEquals(11, tParsed.getMinute());
		assertEquals(33, tParsed.getSecond());

		assertTrue(t.matches("2:11"));
	}

	/**
	 */
	public void testConstant() {
		ConstantFormat con = new ConstantFormat("abc");
		con.matches("abc");
		assertEquals("abc", con.get());
	}

	/**
	 */
	public void testConstantAndNumbers() {
		ConstantFormat con = new ConstantFormat("abc ");
		NumberFormat number = new NumberFormat();
		Format f = new ComposedFormat(con, number);
		f.matches("abc 1234");
		assertEquals("abc ", con.get());
		assertEquals(1234, number.get());
	}

	/**
	 * use strings as format
	 */
	public void testInterpretatedObjects() {
		Format num = new NumberFormat();
		Format f = new ComposedFormat("value: ", num);
		f.matches("value: 100");
		assertEquals(100, num.get());
	}

	/**
	 * check for intervall of number
	 */
	public void testIntervall() {
		IntervallFormat i = new IntervallFormat(10, 99);
		i.matches("45");
		assertEquals(45, i.get());
		assertFalse(i.matches("0"));
		assertEquals(null, i.get());
	}

	/**
	 * 
	 */
	public void testNumberFormat() {
		NumberFormat number = new NumberFormat();
		number.matches("1234");
		assertEquals(1234, number.get());
	}

	/**
	 */
	public void testTime() {
		TimeHMS t = new TimeHMS();
		assertTrue(t.matches("23:59:30"));
		Time tParsed = t.getTime();
		assertEquals(23, tParsed.getHour());
		assertEquals(59, tParsed.getMinute());
		assertEquals(30, tParsed.getSecond());

		TimeHM t2 = new TimeHM();
		assertFalse(t2.matches("11:45:00"));
		assertTrue(t2.matches("11:45"));
		Time tParsed2 = t2.getTime();
		assertEquals(11, tParsed2.getHour());
		assertEquals(45, tParsed2.getMinute());
		assertEquals(00, tParsed2.getSecond());
	}
}
