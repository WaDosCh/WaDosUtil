package ch.judos.generic.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * implements HashMap&lt;K,HashSet&lt;V&gt;&gt;
 * 
 * @since 28.01.2015
 * @author Julian Schelker
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public class HashMapSet<K, V> {

	protected HashMap<K, HashSet<V>> map;

	public HashMapSet() {
		this.map = new HashMap<>();
	}

	public void put(K key, V value) {
		if (!this.map.containsKey(key))
			this.map.put(key, new HashSet<V>());
		this.map.get(key).add(value);
	}

	public HashSet<V> getSet(K key) {
		return this.map.get(key);
	}

	public void removeKey(K key) {
		this.map.remove(key);
	}

	public void removeValue(V value) {
		for (HashSet<V> key : this.map.values())
			key.remove(value);
	}

	public boolean containsKey(K key) {
		return this.map.containsKey(key);
	}

	public Set<K> keys() {
		return this.map.keySet();
	}
}
