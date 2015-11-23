package ch.judos.generic.data.rstorage.tests;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.RSerializerException;
import ch.judos.generic.data.rstorage.tests.objects.ConstantSerialization;

/**
 * @since 09.05.2015
 * @author Julian Schelker
 */
public class EscapingTest extends SerializationTest {

	@Override
	protected Object getTestObject() {
		return new Object[]{new ConstantSerialization("Hallo"),
			new ConstantSerialization("(tag)"), new ConstantSerialization("(=4 )"),
			new ConstantSerialization("\\brac"), new ConstantSerialization("{bla}")};
	}

	public static void main(String[] args) throws IOException, RSerializerException {
		new EscapingTest().outputTestObject();
	}

}
