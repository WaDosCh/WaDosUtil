package ch.judos.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * implements HashMap [K,ArrayList[V]]
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
		ArrayList<V> list = this.map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
			this.map.put(key, list);
		}
		list.add(value);
	}

	public ArrayList<V> getList(K key) {
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

	public Set<K> keys() {
		return this.map.keySet();
	}

	public void clear() {
		this.map.clear();
	}

	public int sizeKeys() {
		return this.map.size();
	}

	public int sizeAllValues() {
		return this.map.values().stream().map(list -> list.size()).reduce(Integer::sum).get();
	}
}
