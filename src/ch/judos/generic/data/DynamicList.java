package ch.judos.generic.data;

import java.lang.reflect.Array;
import java.util.*;

import ch.judos.generic.reflection.Classes;

/**
 * an helpful extension to arraylist
 * 
 * @since 23.06.2011
 * @author Julian Schelker
 * @version 1.11 / 21.02.2013
 * @param <T>
 *            any object to be in the list
 */
public class DynamicList<T> extends ArrayList<T> {

	/**
	 * create an empty list
	 */
	public DynamicList() {
		super();
	}

	/**
	 * create an empty list with some space
	 * 
	 * @param initialCapacity
	 *            initial size to allocate for this list
	 */
	public DynamicList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * @param initialObjects
	 *            initialize the list with elements
	 */
	public DynamicList(Iterable<T> initialObjects) {
		super();
		for (T element : initialObjects) {
			this.add(element);
		}
	}

	/**
	 * @param initialObjects
	 *            added to the list when constructing it
	 */
	@SafeVarargs
	public DynamicList(T... initialObjects) {
		super();
		for (T element : initialObjects) {
			this.add(element);
		}
	}

	/**
	 * list is modified and returned to add further objects
	 * 
	 * @param obj
	 *            add some object
	 * @return the list itself
	 */
	public DynamicList<T> a(T obj) {
		this.add(obj);
		return this;
	}

	/**
	 * @param furtherObjects
	 *            adds all elements to the list
	 */
	@SafeVarargs
	public final void addAll(T... furtherObjects) {
		for (T element : furtherObjects) {
			this.add(element);
		}
	}

	/**
	 * @param <Type>
	 *            type reference
	 * @param c
	 *            class reference
	 * @return a list with all objects from this list that could be casted to
	 *         the given type
	 */
	@SuppressWarnings({"unused", "unchecked"})
	public <Type> DynamicList<Type> castOrOmit(Class<Type> c) {
		DynamicList<Type> result = new DynamicList<>();
		for (T entry : this) {
			try {
				result.add((Type) entry);
			}
			catch (Exception e) {
				// omit item
			}
		}
		return result;
	}

	/**
	 * @param glue
	 * @return all elements toString() glued together
	 */
	public String concatToString(String glue) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			T e = get(i);
			if (e != null)
				b.append(e.toString());
			else
				b.append("null");
			if (i < size() - 1)
				b.append(glue);
		}
		return b.toString();
	}

	/**
	 * <b>Note:</b> will not affect this list
	 * 
	 * @param c
	 * @return only the objects that are instances of the given class (tests for
	 *         class.equals(class))
	 */
	public DynamicList<T> filterByClass(Class<?> c) {
		DynamicList<T> result = new DynamicList<>();
		for (T entry : this) {
			if (entry != null)
				if (entry.getClass().equals(c))
					result.add(entry);
		}
		return result;
	}

	/**
	 * @return a set with the same entries as this list
	 */
	public Set<T> toSet() {
		Set<T> s = new HashSet<>();
		s.addAll(this);
		return s;
	}

	private static final long serialVersionUID = 5577347360761404931L;

	/**
	 * @param <Type>
	 * @param list
	 * @return the list as iterable object
	 */
	public static <Type> Iterable<Type> getIterableObject(final List<Type> list) {
		if (list == null)
			return new EmptyIterable<Type>();
		return new Iterable<Type>() {

			@Override
			public Iterator<Type> iterator() {
				return list.iterator();
			}
		};
	}

	/**
	 * @param list
	 * @return an specific iterator to traverse two elements at a time
	 */
	@SuppressWarnings("rawtypes")
	public static Iterator2 getIterator2AtOnce(List list) {
		if (list.size() < 2)
			throw new RuntimeException("List is assumed to have at least 2 elements.");
		return new Iterator2(list);
	}

	public T getOrNull(int index) {
		if (this.size() > index)
			return get(index);
		return null;
	}

	/**
	 * @param <Type>
	 * @param list
	 * @param search
	 *            the string to look for in the toString() of the objects
	 * @return the object that is found - null if none is found
	 */
	public static <Type> Type searchExact(List<Type> list, String search) {
		for (Type o : list) {
			String s = "null";
			if (o != null)
				s = o.toString();
			if (s.equals(search))
				return o;
		}
		return null;
	}

	/**
	 * @param <X>
	 * @param elements
	 * @return the smallest element of the list, according to their natural
	 *         order
	 */
	public static <X extends Comparable<X>> Optional<X> smallest(List<X> elements) {
		return elements.stream().min(Comparator.naturalOrder());
	}

	/**
	 * @param <X>
	 * @param elements
	 * @return the smallest element of the list
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <X> X smallest(X... elements) {
		List l = Arrays.asList(elements);
		Collections.sort(l);
		return (X) l.get(0);
	}

	/**
	 * @param list
	 * @return a string representation of the given list
	 */
	public static String toString(List<Object> list) {
		StringBuffer buf = new StringBuffer();
		buf.append("List (s:" + list.size() + "): ");
		if (list.size() > 0) {
			for (Object o : list)
				buf.append(o.toString() + ",");
			buf.setLength(buf.length() - 1);
		}
		else {
			buf.append("<<leer>>");
		}
		return buf.toString();
	}

	static class EmptyIterable<T> implements Iterable<T> {

		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {

				@Override
				public boolean hasNext() {
					return false;
				}

				@Override
				public T next() {
					return null;
				}

			};
		}
	}

	/**
	 * a special iterator to get two elements at once
	 * 
	 * @since 23.02.2013
	 * @author Julian Schelker
	 * @version 1.0 / 23.02.2013
	 */
	@SuppressWarnings("rawtypes")
	public static class Iterator2 implements Iterator {

		private List list;
		private int pos;
		private Class<?> commonClass;

		/**
		 * @param list
		 *            list to iterate over
		 */
		public Iterator2(List list) {
			this.list = list;
			this.commonClass = Classes.commonSuperClassOfObjects(list);
			this.pos = 0;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return this.pos < this.list.size() - 1;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Object next() {
			Object[] arr = (Object[]) Array.newInstance(this.commonClass, 2);
			arr[0] = this.list.get(this.pos);
			arr[1] = this.list.get(this.pos + 1);
			this.pos++;
			return arr;
		}

		/**
		 * @deprecated this is not supported
		 */
		@Deprecated
		@Override
		public void remove() {
			throw new RuntimeException("not supported for Iterator2");
		}

	}

	public <K> T searchExactFirst(K search, Mapper<T, K> mapper) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			T e = it.next();
			if (mapper.get(e).equals(search))
				return e;
		}
		return null;
	}

	public static <T2, T1 extends T2> DynamicList<T2> castDown(List<T1> l) {
		DynamicList<T2> result = new DynamicList<>(l.size());
		for (int i = 0; i < l.size(); i++) {
			result.add(l.get(i));
		}
		return result;
	}

	/**
	 * will throw an exception when one object can't be cast to the target class
	 * 
	 * @param l
	 *            list of objects
	 * @param class1
	 *            target class to cast each object to
	 * @return a new dynamic list of T2 objects
	 */
	@SuppressWarnings({"unchecked"})
	public static <T1, T2 extends T1> DynamicList<T2> castUp(List<T1> l, Class<T2> class1)
		throws ClassCastException {
		DynamicList<T2> result = new DynamicList<>(l.size());
		for (int i = 0; i < l.size(); i++) {
			T1 e = l.get(i);
			if (class1.isInstance(e))
				result.add((T2) e);
			else
				throw new ClassCastException("Can't cast object " + e + " to class " + class1);
		}
		return result;
	}

	public T getRandomElement() {
		int index = RandomJS.getInt(this.size() - 1);
		return this.get(index);
	}

}
