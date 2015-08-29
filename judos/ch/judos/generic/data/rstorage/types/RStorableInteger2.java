package ch.judos.generic.data.rstorage.types;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 23.04.2015
 * @author Julian Schelker
 */
public class RStorableInteger2 implements RStorableWrapper {

	private int nr;

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.nr;
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RStorableInteger2 other = (RStorableInteger2) obj;
		if (this.nr != other.nr)
			return false;
		return true;
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
	public void read(CheckReader2 r, RStoreInternal storage) throws IOException {
		this.nr = Integer.valueOf(r.readWhile(c -> Character.isDigit(c) || c == '-'));
	}

	@Override
	public void store(RuntimeWriter2 w, RStoreInternal storage) throws IOException {
		w.write(String.valueOf(this.nr));
	}

	@Override
	public void initWrapped(Object o) {
		this.nr = (Integer) o;
	}

	@Override
	public Object getWrapped() {
		return this.nr;
	}
}
