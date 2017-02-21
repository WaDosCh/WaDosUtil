package ch.judos.generic.data;

import junit.framework.TestCase;

/**
 * @since 18.07.2014
 * @author Julian Schelker
 */
public class StringUtilsTests extends TestCase {

	public void testCountMatches() {

		assertTrue(StringUtils.countMatches("abcdef", "a") == 1);
		assertTrue(StringUtils.countMatches("aaaa", "a") == 4);
		assertTrue(StringUtils.countMatches("abcdef", "g") == 0);
		assertTrue(StringUtils.countMatches("hallo welt", "welt") == 1);
		assertTrue(StringUtils.countMatches("hallo hallo", "welt") == 0);
		assertTrue(StringUtils.countMatches("hallo hallo", "hallo") == 2);
	}

}
