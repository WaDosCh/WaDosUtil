package ch.judos.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @since 09.04.2014
 * @author Julian Schelker
 */
public class Map {

	public static <T> void add(HashMap<T, Integer> map, T key, int amount) {
		if (map.containsKey(key))
			map.put(key, map.get(key) + amount);
		else
			map.put(key, amount);
	}

	public static <T> void add(HashMap<T, Float> map, T key, float amount) {
		if (map.containsKey(key))
			map.put(key, map.get(key) + amount);
		else
			map.put(key, amount);
	}

	@SafeVarargs
	public static <T> HashMap<String, ArrayList<T>> listUp(HashMap<String, T>... maps) {
		HashMap<String, ArrayList<T>> result = new HashMap<>();
		for (HashMap<String, T> map : maps) {
			for (Entry<String, T> e : map.entrySet()) {
				ArrayList<T> list = result.get(e.getKey());
				if (list == null) {
					list = new ArrayList<>();
					result.put(e.getKey(), list);
				}
				list.add(e.getValue());
			}
		}
		return result;
	}
}
