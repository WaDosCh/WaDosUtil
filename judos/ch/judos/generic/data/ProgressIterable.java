package ch.judos.generic.data;

import java.util.Iterator;
import java.util.List;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 * @param <T>
 */
public class ProgressIterable<T> implements Iterable<Progress<T>> {

	private T[]	arr;

	/**
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public ProgressIterable(List<T> list) {
		this((T[]) list.toArray());
	}

	/**
	 * @param arr
	 */
	public ProgressIterable(T[] arr) {
		this.arr = arr;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Progress<T>> iterator() {
		return new Iterator<Progress<T>>() {
			int	index	= 0;

			public boolean hasNext() {
				return this.index < ProgressIterable.this.arr.length;
			}

			public Progress<T> next() {
				float progress = (float) this.index / ProgressIterable.this.arr.length;
				return new Progress<T>(progress, ProgressIterable.this.arr[this.index++]);
			}

			public void remove() {
			}

		};
	}

}