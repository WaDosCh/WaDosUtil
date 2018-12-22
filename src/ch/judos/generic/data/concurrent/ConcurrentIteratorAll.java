package ch.judos.generic.data.concurrent;

import java.util.Iterator;
import java.util.List;

/**
 * @since 09.03.2016
 * @author Julian Schelker
 */
public class ConcurrentIteratorAll<E> implements Iterator<E> {

	int cursor; // index of next element to return
	private List<E> list;
	private E lastElement;

	public ConcurrentIteratorAll(List<E> l) {
		this.list = l;
		this.cursor = 0;
		this.lastElement = null;
	}

	@Override
	public boolean hasNext() {
		return this.cursor < this.list.size();
	}

	@Override
	public E next() {
		try {
			this.lastElement = this.list.get(this.cursor);
		}
		catch (IndexOutOfBoundsException e) {
			return null;
		}
		this.cursor++;
		return this.lastElement;
	}

	@Override
	public void remove() {
		try {
			this.list.remove(this.lastElement);
		}
		catch (IndexOutOfBoundsException ex) {
			// element was most likely already deleted, unsafe
		}
	}
}
