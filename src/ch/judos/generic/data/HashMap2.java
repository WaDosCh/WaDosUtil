package ch.judos.generic.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * a hashmap with two keys
 * 
 * @since 23.02.2013
 * @author Julian Schelker
 * @version 1.0 / 23.02.2013
 * @param <K> key of the hashmap
 * @param <K2> second key of the hashmap
 * @param <V> value associated to the two keys
 */
public class HashMap2<K, K2, V> {

	/**
	 * the map
	 */
	protected HashMap<K, HashMap<K2, V>> map1;

	/**
	 * creates the map
	 */
	public HashMap2() {
		this.map1 = new HashMap<>();
	}

	/**
	 * @param key1 first key
	 * @param key2 second key
	 * @param value value to associate to those two keys puts a value to the specified two keys
	 */
	public void put(K key1, K2 key2, V value) {
		HashMap<K2, V> map2 = this.map1.get(key1);
		if (map2 == null) {
			map2 = new HashMap<>();
			this.map1.put(key1, map2);
		}
		map2.put(key2, value);
	}

	/**
	 * @param key1 first key
	 * @param key2 second key
	 * @return gets the value back, needs both keys
	 */
	public V get(K key1, K2 key2) {
		HashMap<K2, V> map2 = this.map1.get(key1);
		if (map2 == null)
			return null;
		return map2.get(key2);
	}

	/**
	 * @param key1 the first key
	 * @param key2 the second key
	 * @return true if the given two keys exist
	 */
	public boolean containsKey(K key1, K2 key2) {
		if (this.map1.containsKey(key1)) {
			return this.map1.get(key1).containsKey(key2);
		}
		return false;
	}

	public Set<K> keySet() {
		return this.map1.keySet();
	}

	public Set<K2> getInnerKeySet(K key) {
		HashMap<K2, V> map = this.map1.get(key);
		if (map == null)
			return Collections.emptySet();
		return map.keySet();
	}

	public void clear() {
		this.map1.clear();
	}
}
