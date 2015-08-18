package ch.judos.generic.data.rstorage.tests;

import java.io.*;
import java.util.Arrays;

import junit.framework.TestCase;
import ch.judos.generic.data.rstorage.ReadableStorage2;
import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 20.04.2015
 * @author Julian Schelker
 */
public abstract class SerializationTest extends TestCase {

	public void testObjectsSerializeAndDeserialize() throws IOException, RSerializerException {
		Object obj = getTestObject();
		if (obj.getClass().isArray()) {
			Object[] arr = (Object[]) obj;
			for (Object object : arr)
				testOneObject(object);
			return;
		}
		testOneObject(obj);
	}

	public void testOneObject(Object obj) throws RSerializerException, IOException {
		ReadableStorage2 r = ReadableStorage2.getNewStorage();

		StringWriter out = new StringWriter();
		BufferedWriter writer = new BufferedWriter(out);
		r.store(obj, writer);
		writer.flush();
		String x1 = out.getBuffer().toString();

		BufferedReader reader = new BufferedReader(new StringReader(x1));
		Object retrieved = r.read(reader);
		reader.close();

		if (obj.getClass().isArray()) {
			Object[] objA = (Object[]) obj;
			boolean ok = Arrays.equals(objA, (Object[]) retrieved);
			assertTrue("Objects " + obj + " and " + retrieved + " are not equal.", ok);
		}
		else
			assertEquals("Objects " + obj + " and " + retrieved + " are not equal.", obj,
				retrieved);

		out.getBuffer().setLength(0);
		r.store(retrieved, writer);
		writer.flush();
		String x2 = out.getBuffer().toString();
		writer.close();

		assertEquals("Objects " + x1 + " and " + x2 + " didn't serialize the same way", x1, x2);
	}

	protected abstract Object getTestObject();

	protected void outputTestObject() throws IOException, RSerializerException {
		ReadableStorage2 rs = ReadableStorage2.getNewStorage();

		BufferedWriter b2 = new BufferedWriter(new OutputStreamWriter(System.out));
		Object obj = getTestObject();
		if (!obj.getClass().isArray()) {
			obj = new Object[]{obj};
		}
		Object[] arr = (Object[]) obj;
		for (Object o : arr) {
			rs.store(o, b2);
			b2.flush();
			b2.write("\n\n");
		}
	}
}
