package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 27.04.2015
 * @author Julian Schelker
 */
public class ArrayTest extends SerializationTest {

	@Override
	protected Object getTestObject() {
		return new Object[]{new Integer[]{1, 2}, new Object[0], new String[]{"Hello"},
			new Object[]{1, "Hallo"}};
	}

	public static void main(String[] args) throws IOException, RSerializerException {
		new ArrayTest().outputTestObject();
	}

}
