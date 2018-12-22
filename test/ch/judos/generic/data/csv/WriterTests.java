package ch.judos.generic.data.csv;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * @author Julian Schelker
 * @version 1.0
 * @lastUpdate 04.01.2012
 * @dependsOn
 */
public class WriterTests extends TestCase {

	private CSVFileWriter csv;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		this.csv = new CSVFileWriter(new String[]{"A", "B", "C"});
		this.csv.addEntry(new String[]{"1", "1", "1"});
		this.csv.addEntry(new String[]{"2", "2", "2"});
	}

	public void testWritingToOutputStream() throws IOException {
		StringWriter o = new StringWriter();
		this.csv.write(o);

		String s = o.getBuffer().toString();
		String c = CSVFile.separator;
		String l = CSVFile.linebreak;

		String expected = "A" + c + "B" + c + "C" + l + "1" + c + "1" + c + "1" + l + "2" + c
			+ "2" + c + "2" + l;

		assertEquals(s, expected);
	}

}
