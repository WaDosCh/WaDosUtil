package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 06.05.2015
 * @author Julian Schelker
 */
public class ObjectReferenceTests extends SerializationTest {
	static String a = "Hallo";
	static String b = "Welt";
	static String c = a + "";

	public static void main(String[] args) throws IOException, RSerializerException {
		new ObjectReferenceTests().outputTestObject();
	}

	@Override
	protected Object getTestObject() {
		return new Object[][]{new String[]{a, b, c, b}, new Object[]{1, 1, a, c}};
	}
}
