package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 23.04.2015
 * @author Julian Schelker
 */
public class IntegerTest extends SerializationTest {

	@Override
	protected Object getTestObject() {
		Integer[] x = {0, 5, 32, 1000, -8};
		return x;
	}

	public static void main(String[] args) throws IOException, RSerializerException {
		new IntegerTest().outputTestObject();
	}
}
