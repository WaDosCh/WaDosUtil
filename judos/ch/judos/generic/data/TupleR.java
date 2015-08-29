/**
 * 
 */
package ch.judos.generic.data;

import java.util.Arrays;

/**
 * A tuple with only read access.
 * 
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.1 / 23.02.2013
 * @param <T0>
 * @param <T1>
 */
public class TupleR<T0, T1> implements CloneableJS {

	/**
	 * first entry
	 */
	public T0 e0;
	/**
	 * second entry
	 */
	public T1 e1;

	/**
	 * @param e0
	 * @param e1
	 */
	public TupleR(T0 e0, T1 e1) {
		this.e0 = e0;
		this.e1 = e1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TupleR<T0, T1> clone() throws CloneNotSupportedException {
		return new TupleR<>(this.e0, this.e1);
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
		if (!(obj instanceof TupleR))
			return false;
		TupleR<?, ?> t = (TupleR<?, ?>) obj;
		return this.e0.equals(t.e0) && this.e1.equals(t.e1);
	}

	/**
	 * @param index
	 *            0 based index
	 * @return the n'th element
	 */
	public Object get(int index) {
		if (index == 0) {
			return this.e0;
		}
		else if (index == 1)
			return this.e1;
		else
			throw new IndexOutOfBoundsException("only index 0 and 1 is allowed.");
	}

	/**
	 * @return first entry
	 */
	public T0 get0() {
		return this.e0;
	}

	/**
	 * @return second entry
	 */
	public T1 get1() {
		return this.e1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[]{this.e0, this.e1});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + this.e0 + ", " + this.e1 + "]";
	}

}
