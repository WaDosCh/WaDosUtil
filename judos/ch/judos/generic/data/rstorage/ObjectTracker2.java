package ch.judos.generic.data.rstorage;

import java.util.HashMap;

import ch.judos.generic.data.HashMapR;
import ch.judos.generic.data.Map;

/**
 * @since 16.10.2014
 * @author Julian Schelker
 */
public class ObjectTracker2 {
	HashMapR<Object, Integer> map;
	HashMap<Object, Integer> usages;
	private int nextIndex;

	public ObjectTracker2() {
		this.map = new HashMapR<>();
		this.usages = new HashMap<>();
		this.nextIndex = 0;
	}

	public boolean isMappedAlready(Object o) {
		return this.map.containsKey(o);
	}

	public int getIndexForObject(Object o) {
		Map.add(this.usages, o, 1);
		return this.map.getFromKey(o);
	}

	public Object getObjectForIndex(int index) {
		return this.map.getFromValue(index);
	}

	public int getUsageForObject(Object o) {
		return this.usages.get(o);
	}

	/**
	 * @param o
	 * @return the index the object is mapped to
	 */
	public int mapObjectAndGetIndex(Object o) {
		this.map.put(o, this.nextIndex);
		this.usages.put(o, 0);
		return this.nextIndex++; // returns the index and then increments
	}

	public void mapObjectToIndex(Object value, int index) {
		this.map.put(value, index);
	}
}
