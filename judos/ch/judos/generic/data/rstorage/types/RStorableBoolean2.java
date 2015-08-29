package ch.judos.generic.data.rstorage.types;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 21.10.2014
 * @author Julian Schelker
 */
public class RStorableBoolean2 implements RStorableWrapper {

	private boolean c;

	@Override
	public boolean equals(Object obj) {
		return this.c == ((RStorableBoolean2) obj).c;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public void read(CheckReader2 r, RStoreInternal storage) throws IOException {
		this.c = Boolean.valueOf(r.readUntilNonCharacter());
	}

	@Override
	public void store(RuntimeWriter2 w, RStoreInternal storage) throws IOException {
		w.write(String.valueOf(this.c));
	}

	@Override
	public boolean isTrackedAsObject() {
		return false;
	}

	@Override
	public boolean showOnOneLine() {
		return true;
	}

	@Override
	public void initWrapped(Object o) {
		this.c = (Boolean) o;
	}

	@Override
	public Boolean getWrapped() {
		return this.c;
	}

}
