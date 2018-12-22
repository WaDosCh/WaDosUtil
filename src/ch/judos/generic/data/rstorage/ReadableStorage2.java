package ch.judos.generic.data.rstorage;

import java.io.Reader;
import java.io.Writer;

import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 20.04.2015
 * @author Julian Schelker
 */
public interface ReadableStorage2 {

	public static ReadableStorage2 getNewStorage() {
		return new ReadableStorage2Impl();
	}

	public Object read(Reader r) throws RSerializerException;
	public void store(Object o, final Writer w) throws RSerializerException;

}
