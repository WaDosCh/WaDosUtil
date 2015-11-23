package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 26.04.2015
 * @author Julian Schelker
 */
public class StringTest extends SerializationTest {

	@Override
	protected Object getTestObject() {
		return new String[]{"Hallo", "", "a\"b"};
	}

	public static void main(String[] args) throws IOException, RSerializerException {
		new StringTest().outputTestObject();
	}

}
