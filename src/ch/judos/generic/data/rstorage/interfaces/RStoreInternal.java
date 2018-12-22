package ch.judos.generic.data.rstorage.interfaces;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;

/**
 * @since 03.05.2015
 * @author Julian Schelker
 */
public interface RStoreInternal {

	public Object read(CheckReader2 r) throws IOException;

	/**
	 * to deserialize this object you must use read(reader,assumeType) with the
	 * correct type given. <br>
	 * deserializer finds an typeless object and no assumeType is given an
	 * Exception will be thrown
	 * 
	 * @param o
	 * @param w
	 * @param storeType
	 * @throws IOException
	 */
	public void store(Object o, final RuntimeWriter2 w, boolean storeType) throws IOException;

	public Object read(CheckReader2 r, Class<?> assumeType) throws IOException;
}
