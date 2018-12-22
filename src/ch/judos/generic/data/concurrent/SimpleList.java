package ch.judos.generic.data.concurrent;

import java.util.Iterator;

import ch.judos.generic.data.DynamicList;

/**
 * a list that optimistically removes ConcurrentModificationExceptions from the
 * implementation of all ArrayList methods.
 * 
 * @deprecated use ConcurrentIteratorAll instead
 * @since 01.02.2015
 * @author Julian Schelker
 * @param <T>
 *            contained in this list
 */
@Deprecated
public class SimpleList<T> extends DynamicList<T> {

	private static final long serialVersionUID = -5788620750829673564L;

	/**
	 * <p>
	 * The returned iterator does not fail.
	 *
	 * @return an iterator over the elements in this list in proper sequence
	 */
	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}

	/**
	 * An optimized version of AbstractList.Itr
	 */
	protected class Itr implements Iterator<T> {
		int cursor; // index of next element to return

		@Override
		public boolean hasNext() {
			return this.cursor < size();
		}

		@Override
		public T next() {
			T element;
			try {
				element = get(this.cursor);
			}
			catch (IndexOutOfBoundsException e) {
				return null;
			}
			this.cursor++;
			return element;
		}

		@Override
		public void remove() {
			try {
				SimpleList.this.remove(this.cursor);
			}
			catch (IndexOutOfBoundsException ex) {
				// element was most likely already deleted, unsafe
			}
		}

	}
}
