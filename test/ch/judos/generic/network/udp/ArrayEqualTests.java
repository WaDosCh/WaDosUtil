package ch.judos.generic.network.udp;

import junit.framework.TestCase;

/**
 * @since 05.07.2013
 * @author Julian Schelker
 */
public class ArrayEqualTests extends TestCase {

	public void testArrayEquals() {
		byte[] b1 = new byte[]{1, 2, 3};
		byte[] b2 = new byte[]{1, 2, 3};

		// equals does not work for arrays
		if (b1.equals(b2)) {
			fail();
		}
	}

}
