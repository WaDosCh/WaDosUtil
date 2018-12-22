package ch.judos.generic.control;

import java.util.HashMap;

/**
 * @since 28.11.2015
 * @author Julian Schelker
 */
public class Global {

	private static HashMap<String, String> properties;

	public static void set(String key, String value) {
		init();
		properties.put(key, value);
	}

	public static String get(String key) {
		init();
		return properties.get(key);
	}

	private static void init() {
		if (properties == null)
			properties = new HashMap<String, String>();
	}
}
