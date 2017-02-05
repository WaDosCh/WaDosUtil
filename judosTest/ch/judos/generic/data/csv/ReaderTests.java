package ch.judos.generic.data.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * @created 04.01.2012
 * @author Julian Schelker
 * @version 1.0
 * @lastUpdate 04.01.2012
 * @dependsOn
 */
public class ReaderTests extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	private StringReader input;

	@Override
	protected void setUp() throws Exception {
		this.input = new StringReader("A;B;C\n1;1;1\n2;2;2");
	}

	public void testFile() throws IOException {
		CSVFileReader csv = CSVFileReader.read(this.input);
		assertTrue(Arrays.equals(csv.getAttributes(), new String[]{"A", "B", "C"}));
		assertEquals(csv.countEntries(), 2);
		HashMap<String, String> entry = csv.getEntry(0);
		assertEquals(entry.get("A"), "1");
		assertEquals(entry.get("B"), "1");
		assertEquals(entry.get("C"), "1");
		entry = csv.getEntry(1);
		assertEquals(entry.get("A"), "2");
	}

}
