package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 26.04.2015
 * @author Julian Schelker
 */
public class FloatTest extends SerializationTest {

	@Override
	protected Object getTestObject() {
		return new Float[]{0f, 1f, 0.25f, -0.25f, Float.MAX_VALUE, Float.MIN_VALUE,
			Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NaN};
	}

	public static void main(String[] args) throws IOException, RSerializerException {
		new FloatTest().outputTestObject();
	}
}
