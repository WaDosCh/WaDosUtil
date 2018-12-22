package ch.judos.generic.data;

import ch.judos.generic.data.Serializer;
import junit.framework.TestCase;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class SerializerTests extends TestCase {
	public void testShort() {

		short[] test = new short[]{5, -1, 0, 1, 1000, 32767, -32768, 30000};
		for (short s : test) {
			byte[] sB = new byte[2];
			Serializer.short2bytes(sB, 0, s);
			short s2 = Serializer.bytes2short(sB, 0);
			assertEquals(s, s2);
		}
	}
}
