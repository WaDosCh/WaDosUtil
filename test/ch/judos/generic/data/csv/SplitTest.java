package ch.judos.generic.data.csv;

import junit.framework.TestCase;

/**
 * @author Julian Schelker
 * @version 1.0
 * @lastUpdate 04.01.2012
 * @dependsOn
 */
public class SplitTest extends TestCase {
	public void testSplit() {
		String s1 = "a;b;c";
		String s2 = "A;;";
		String s3 = ";;C";

		assertEquals(s1.split(";", -1).length, 3);
		assertEquals(s2.split(";", -1).length, 3);
		assertEquals(s3.split(";", -1).length, 3);
	}
}
