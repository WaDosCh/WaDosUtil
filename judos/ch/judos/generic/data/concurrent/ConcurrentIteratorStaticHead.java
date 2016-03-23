package ch.judos.generic.data.concurrent;

import java.util.Iterator;
import java.util.List;

/**
 * starts at the end of the list and goes to the head. the current element can
 * be removed and elements can be added at the end of the list without any
 * problems.
 * 
 * @since 09.03.2016
 * @author Julian Schelker
 */
public class ConcurrentIteratorStaticHead<E> implements Iterator<E> {

	int cursor; // index of next element to return
	private List<E> list;
	private E lastElement;

	public ConcurrentIteratorStaticHead(List<E> l) {
		this.list = l;
		this.cursor = l.size() - 1;
		this.lastElement = null;
	}

	@Override
	public boolean hasNext() {
		return this.cursor >= 0;
	}

	@Override
	public E next() {
		try {
			this.lastElement = this.list.get(this.cursor);
		}
		catch (IndexOutOfBoundsException e) {
			return null;
		}
		this.cursor--;
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
