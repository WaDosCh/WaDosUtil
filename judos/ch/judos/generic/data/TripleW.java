package ch.judos.generic.data;

/**
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.1 / 22.02.2013
 * @param <T0>
 * @param <T1>
 * @param <T2>
 */
public class TripleW<T0, T1, T2> {
	/**
	 * first entry
	 */
	public T0 e0;
	/**
	 * second entry
	 */
	public T1 e1;
	/**
	 * third entry
	 */
	public T2 e2;

	/**
	 * @param e0
	 * @param e1
	 * @param e2
	 */
	public TripleW(T0 e0, T1 e1, T2 e2) {
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
	}

	/**
	 * @param index
	 *            0 based index
	 * @return the n'th element
	 */
	public Object get(int index) {
		if (index == 0)
			return this.e0;
		else if (index == 1)
			return this.e1;
		else if (index == 2)
			return this.e2;
		else
			throw new IndexOutOfBoundsException("only index 0, 1 and 2 is allowed.");
	}

	/**
	 * @return the first entry
	 */
	public T0 get0() {
		return this.e0;
	}

	/**
	 * @return the second entry
	 */
	public T1 get1() {
		return this.e1;
	}

	/**
	 * @return the third entry
	 */
	public T2 get2() {
		return this.e2;
	}

	/**
	 * replaces the first element with this datatype to the new object
	 * 
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public void set(Object e) {
		try {
			this.e0 = (T0) e;
		}
		catch (Exception exc) {
			try {
				this.e1 = (T1) e;
			}
			catch (Exception exc2) {
				this.e2 = (T2) e;
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + this.e0 + ", " + this.e1 + ", " + this.e2 + "]";
	}

}
