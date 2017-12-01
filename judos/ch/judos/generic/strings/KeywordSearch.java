package ch.judos.generic.strings;

import java.util.Arrays;
import java.util.function.Function;

import ch.judos.generic.data.TupleR;

public class KeywordSearch<T> {

	public double score(String t1, String t2) {
		KeywordPhrase phrase1 = new KeywordPhrase(t1);
		KeywordPhrase phrase2 = new KeywordPhrase(t2);
		return phrase1.score(phrase2);
	}

	public KeywordMatch<T> bestMatch(String search, T[] objects,
		Function<T, String> toString) {
		if (objects.length == 0)
			return null;
		@SuppressWarnings("unchecked")
		TupleR<Integer, Double>[] matches = new TupleR[objects.length];
		for (int i = 0; i < objects.length; i++) {
			String t2 = toString.apply(objects[i]);
			double currentScore = score(search, t2);
			matches[i] = new TupleR<>(i, currentScore);
		}
		Arrays.sort(matches, (t1, t2) -> t2.e1.compareTo(t1.e1));
		return new KeywordMatch<T>(search, matches, objects, toString);
	}
}
