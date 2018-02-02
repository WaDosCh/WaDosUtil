package ch.judos.generic.data.csv;

import junit.framework.TestCase;

/**
 * @author Julian Schelker
 * @version 1.0
 * @lastUpdate 04.01.2012
 * @dependsOn
 */
public class DecodeEncodeTests extends TestCase {

	public void testDecodeEncode() {
		String[] s = new String[5];

		s[0] = "abc" + CSVFile.separator + "def";
		s[1] = "a" + CSVFile.escape + CSVFile.linebreak + CSVFile.separator + CSVFile.escape;
		s[2] = CSVFile.escape + CSVFile.escape + CSVFile.separator;
		s[3] = CSVFile.separator + CSVFile.separator + CSVFile.escape;
		s[4] = CSVFile.escape + CSVFile.escape + CSVFile.linebreak;

		for (String test : s) {
			assertEquals(test, CSVFile.decodeForValue(CSVFile.encodeForFile(test)));
		}
	}

}
