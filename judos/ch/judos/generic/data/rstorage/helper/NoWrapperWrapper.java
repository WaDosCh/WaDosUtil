package ch.judos.generic.data.rstorage.helper;

import java.io.IOException;

import ch.judos.generic.data.rstorage.interfaces.RStorableManual2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 03.05.2015
 * @author Julian Schelker
 */
public class NoWrapperWrapper implements RStorableWrapper {

	private RStorableManual2 obj;

	public NoWrapperWrapper(RStorableManual2 o) {
		this.obj = o;
	}

	@Override
	public boolean isTrackedAsObject() {
		return this.obj.isTrackedAsObject();
	}

	@Override
	public boolean showOnOneLine() {
		return this.obj.showOnOneLine();
	}

	@Override
	public void read(CheckReader2 r, RStoreInternal storage) throws IOException {
		this.obj.read(r, storage);
	}

	@Override
	public void store(RuntimeWriter2 w, RStoreInternal storage) throws IOException {
		this.obj.store(w, storage);
	}

	@Override
	public void initWrapped(Object o) {
		throw new RuntimeException("initWrapped not supported for NoWrapperWrapper");
	}

	@Override
	public Object getWrapped() {
		return this.obj;
	}

}
