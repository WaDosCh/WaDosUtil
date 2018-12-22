package ch.judos.generic.data;

/**
 * @since 21.10.2014
 * @author Julian Schelker
 * @param <T>
 *            element that the factory produces
 */
public interface Factory<T> {

	public T createInstance();

}
