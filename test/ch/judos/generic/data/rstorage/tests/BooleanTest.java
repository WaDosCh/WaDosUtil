package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 23.04.2015
 * @author Julian Schelker
 */
public class BooleanTest extends SerializationTest {

	@Override
	protected Object getTestObject() {
		Object[] objects = {true, false};
		return objects;
	}

	public static void main(String[] args) throws IOException, RSerializerException {
		new BooleanTest().outputTestObject();
	}
}
