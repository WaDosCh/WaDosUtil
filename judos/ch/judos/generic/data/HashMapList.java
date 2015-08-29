package ch.judos.generic.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * implements HashMap [K,HashSet[V]]
 * 
 * @since 02.02.2015
 * @author Julian Schelker
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public class HashMapList<K, V> {

	protected HashMap<K, ArrayList<V>> map;

	public HashMapList() {
		this.map = new HashMap<>();
	}

	public void put(K key, V value) {
		if (!this.map.containsKey(key))
			this.map.put(key, new ArrayList<V>());
		this.map.get(key).add(value);
	}

	public ArrayList<V> getSet(K key) {
		return this.map.get(key);
	}

	public void removeKey(K key) {
		this.map.remove(key);
	}

	public void removeValue(V value) {
		for (ArrayList<V> key : this.map.values())
			key.remove(value);
	}

	public boolean containsKey(K key) {
		return this.map.containsKey(key);
	}
}
