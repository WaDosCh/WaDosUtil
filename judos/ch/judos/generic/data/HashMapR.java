package ch.judos.generic.data;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * a hashmap working in both directions
 * 
 * @since 29.10.2011
 * @author Julian Schelker
 * @version 1.0 / 23.02.2013
 * @param <K>
 * @param <V>
 */
public class HashMapR<K, V> {

	private HashMap<K, V> key2value;
	private HashMap<V, K> value2key;

	/**
	 * creates the map
	 */
	public HashMapR() {
		this.key2value = new HashMap<>();
		this.value2key = new HashMap<>();
	}

	/**
	 * put some value to the key
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		this.key2value.put(key, value);
		this.value2key.put(value, key);
	}

	/**
	 * @param key
	 * @return true if the key is contained in the map
	 */
	public boolean containsKey(K key) {
		return this.key2value.containsKey(key);
	}

	/**
	 * @param value
	 * @return true if the value is contained in the map
	 */
	public boolean containsValue(V value) {
		return this.value2key.containsKey(value);
	}

	/**
	 * @param key
	 * @return the value found for this key
	 */
	public V getFromKey(K key) {
		return this.key2value.get(key);
	}

	/**
	 * @param value
	 * @return the key found for this value
	 */
	public K getFromValue(V value) {
		return this.value2key.get(value);
	}

	/**
	 * removes the key and the value that belonged to this key
	 * 
	 * @param key
	 */
	public void removeByKey(K key) {
		V value = getFromKey(key);
		this.key2value.remove(key);
		this.value2key.remove(value);
	}

	/**
	 * removes the value and the key that belonged to this value
	 * 
	 * @param value
	 */
	public void removeByValue(V value) {
		K key = getFromValue(value);
		this.key2value.remove(key);
		this.value2key.remove(value);
	}

	public Set<V> getValueSet() {
		return this.value2key.keySet();
	}

	public Set<K> getKeySet() {
		return this.key2value.keySet();
	}

	public Set<Entry<K, V>> entrySet() {
		return this.key2value.entrySet();
	}
}
