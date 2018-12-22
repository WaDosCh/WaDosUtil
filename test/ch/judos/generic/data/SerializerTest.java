package ch.judos.generic.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author Julian Schelker
 */
public class SerializerTest {

	@Test
	public void testObject() throws SerializerException {
		ArrayList<Integer> x = new ArrayList<>();
		x.add(3);
		x.add(10000);
		x.add(-100);
		byte[] data = Serializer.object2Bytes(x);
		Object o = Serializer.bytes2object(data);
		assertEquals(x, o);
	}

	@Test
	public void testXml() throws SerializerException {
		String xml = "<root><type>someTestMessage</type><clients>"
			+ "<client><ip>127.0.0.1</ip><port>50000</port><name>julian</name>"
			+ "</client></clients></root>";
		Document doc = Serializer.text2Document(xml);
		String xml2 = Serializer.document2Text(doc);
		assertEquals(xml, xml2);
	}

	@Test
	public void testIntegers() {
		int[] i = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 1000, -42};
		byte[] data = new byte[6];
		for (int test : i) {
			Serializer.int2bytes(data, 1, test);
			int test2 = Serializer.bytes2int(data, 1);
			assertEquals(test, test2);
		}
	}

}
