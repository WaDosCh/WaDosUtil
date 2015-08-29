package ch.judos.generic.data.rstorage;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RSerializerException;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 03.05.2015
 * @author Julian Schelker
 */
public class RInternalImpl implements RStoreInternal {

	private ReadableStorage2Impl rstorage;

	public RInternalImpl(ReadableStorage2Impl rstorage) {
		this.rstorage = rstorage;
	}

	@Override
	public void store(Object o, RuntimeWriter2 w, boolean storeType) throws IOException {
		try {
			this.rstorage.storeWithTags(o, w, storeType);
		}
		catch (RSerializerException e) {
			this.rstorage.rethrowI("", e);
		}
	}

	@Override
	public Object read(CheckReader2 r) throws IOException {
		return read(r, null);
	}

	@Override
	public Object read(CheckReader2 r, Class<?> assumeType) throws IOException {
		try {
			return this.rstorage.readWithTags(r, assumeType);
		}
		catch (RSerializerException e) {
			this.rstorage.rethrowI("", e);
			return null;
		}
	}
}
