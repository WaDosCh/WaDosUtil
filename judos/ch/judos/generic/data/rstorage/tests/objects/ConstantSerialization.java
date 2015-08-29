package ch.judos.generic.data.rstorage.tests.objects;

import java.io.IOException;
import java.security.InvalidParameterException;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;
import ch.judos.generic.data.rstorage.interfaces.RStorableManual2;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 06.05.2015
 * @author Julian Schelker
 */
public class ConstantSerialization implements RStorableManual2 {

	public static int length = 5;
	private String str;

	public ConstantSerialization(String s) {
		if (s.length() != length)
			throw new InvalidParameterException("String " + s + " must be " + length
				+ " chars long");
		this.str = s;
	}

	@Override
	public boolean isTrackedAsObject() {
		return true;
	}

	@Override
	public boolean showOnOneLine() {
		return true;
	}

	@Override
	public void read(CheckReader2 r, RStoreInternal storage) throws IOException {
		char[] s = new char[length];
		r.read(s, 0, length);
		this.str = new String(s);
	}

	@Override
	public void store(RuntimeWriter2 w, RStoreInternal storage) throws IOException {
		w.write(this.str);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.str == null) ? 0 : this.str.hashCode());
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
		ConstantSerialization other = (ConstantSerialization) obj;
		if (this.str == null) {
			if (other.str != null)
				return false;
		}
		else if (!this.str.equals(other.str))
			return false;
		return true;
	}

}
