package ch.judos.generic.data.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import ch.judos.generic.data.csv.CSVFileReader;
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
		assertTrue(Arrays.equals(csv.getEntry(0), new String[]{"1", "1", "1"}));
		assertTrue(Arrays.equals(csv.getEntry(1), new String[]{"2", "2", "2"}));
	}

}
