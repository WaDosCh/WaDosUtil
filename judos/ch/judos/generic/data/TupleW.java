package ch.judos.generic.data;

/**
 * @since 23.02.2013
 * @author Julian Schelker
 * @version 1.0 / 23.02.2013
 * @param <T0>
 * @param <T1>
 */
public class TupleW<T0, T1> extends TupleR<T0, T1> {

	/**
	 * @param e0
	 * @param e1
	 */
	public TupleW(T0 e0, T1 e1) {
		super(e0, e1);
	}

	/**
	 * replaces the first element where the datatype matches with the new object
	 * 
	 * @param e
	 * @return whether the entry could be changed
	 */
	@SuppressWarnings("unchecked")
	public boolean set(Object e) {
		try {
			this.e0 = (T0) e;
			return true;
		}
		catch (Exception exc) {
			try {
				this.e1 = (T1) e;
				return true;
			}
			catch (Exception exc2) {
				return false;
			}
		}
	}

	/**
	 * @param e0
	 *            new first entry
	 */
	public void set0(T0 e0) {
		this.e0 = e0;
	}

	/**
	 * @param e1
	 *            new second entry
	 */
	public void set1(T1 e1) {
		this.e1 = e1;
	}

}
