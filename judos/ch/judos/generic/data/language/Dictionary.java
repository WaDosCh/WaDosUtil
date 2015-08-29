package ch.judos.generic.data.language;

import java.util.ArrayList;
import java.util.Collections;

/**
 * allows to select a language and returns the interface to get the words in the
 * language
 * 
 * @since 22.02.2013
 * @author Julian Schelker
 * @version 1.0 / 22.02.2013
 */
public class Dictionary {

	/**
	 * array of all language classes<br>
	 * add your language class here!
	 */
	protected static final Class<?>[] languages = new Class<?>[]{English.class, German.class};

	/**
	 * @return all supported languages
	 */
	public static String[] getSupportedLanguages() {
		ArrayList<String> l = new ArrayList<>();
		for (Class<?> c : languages)
			l.add(c.getSimpleName());
		Collections.sort(l);
		return l.toArray(new String[]{});
	}

	/**
	 * select a language to be able to retrieve words for that language
	 * 
	 * @param name
	 * @return the dictionary
	 */
	@SuppressWarnings("unchecked")
	public static LanguageI selectLanguage(String name) {
		try {
			for (Class<?> c : languages)
				if (c.getSimpleName().equalsIgnoreCase(name)) {
					Class<? extends LanguageI> c1 = (Class<? extends LanguageI>) c;
					return c1.newInstance();
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
