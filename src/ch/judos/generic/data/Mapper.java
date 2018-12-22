package ch.judos.generic.data;

/**
 * @since 21.10.2014
 * @author Julian Schelker
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public interface Mapper<K, V> {

	public V get(K key);

}
