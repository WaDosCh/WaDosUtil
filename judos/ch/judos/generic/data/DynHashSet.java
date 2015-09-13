package ch.judos.generic.data;

import java.util.HashSet;

/**
 * @since 17.05.2015
 * @author Julian Schelker
 * @param <E>
 */
public class DynHashSet<E> extends HashSet<E> {

	private static final long serialVersionUID = 682846749781948216L;

	public DynHashSet(E... values) {
		super(values.length + 10);
		for (E value : values)
			this.add(value);
	}

}
