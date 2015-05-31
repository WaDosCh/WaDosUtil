package ch.judos.generic.wrappers;

import java.util.Arrays;

/**
 * can be used to store arrays easily inside HashMap's or HashSet's. The equals
 * and hashCode method is implemented to make sure it uses the array's content
 * 
 * @since 23.05.2015
 * @author Julian Schelker
 * @param <T>
 */
public class WrappedArray<T> {

	public T[]	data;

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.data);
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
		@SuppressWarnings("unchecked")
		WrappedArray<T> other = (WrappedArray<T>) obj;
		if (!Arrays.equals(this.data, other.data))
			return false;
		return true;
	}

}
