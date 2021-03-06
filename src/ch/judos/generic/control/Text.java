package ch.judos.generic.control;

import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.ResourceBundle;

import ch.judos.generic.data.DynamicList;
import ch.judos.java.util.UTF8Control;

public class Text {

	private ResourceBundle bundle;
	private static final Text sharedInstance = new Text();
	private boolean output = false;

	private Text() {
		Locale currentLocale = Locale.getDefault();
		if (this.output)
			System.out.println("Host locale: " + currentLocale);
		LanguageRange high = new LanguageRange(currentLocale.toLanguageTag());

		Locale best = Locale.lookup(new DynamicList<>(high, getDefaultLocale()),
			existingTextPropertyBundles());
		if (this.output)
			System.out.println("Best matching locale: " + best);
		this.bundle = ResourceBundle.getBundle("TextBundle", best, new UTF8Control());
	}

	public LanguageRange getDefaultLocale() {
		DynamicList<Locale> existingLocales = existingTextPropertyBundles();
		return new LanguageRange(existingLocales.get(0).getLanguage(),
			LanguageRange.MIN_WEIGHT);
	}

	public DynamicList<Locale> existingTextPropertyBundles() {
		DynamicList<Locale> result = new DynamicList<Locale>();
		result.add(new Locale("de"));
		return result;
	}

	public static String get(String string, Object... args) {
		try {
			String result = sharedInstance.bundle.getString(string);
			if (args.length > 0) {
				for (Object arg : args) {
					result = result.replaceFirst("\\%", arg.toString());
				}
			}
			return result;
		}
		catch (Exception e) {
			System.err.println("Text key not found: " + string);
			return string;
		}
	}
}
