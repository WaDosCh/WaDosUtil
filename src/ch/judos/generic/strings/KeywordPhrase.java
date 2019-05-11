package ch.judos.generic.strings;

import java.text.Normalizer;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.TupleR;

public class KeywordPhrase {

	private String[] parts;

	public KeywordPhrase(String text) {
		text = text.toLowerCase().replaceAll("[^\\p{L}]", " ").trim();
		text = Normalizer.normalize(text, Normalizer.Form.NFD);
		text = text.replaceAll("\\p{M}", "");
		text = text.replaceAll("\\s+", " ");
		DynamicList<String> stringParts = new DynamicList<>(text.split(" "));
		this.parts = stringParts.stream().filter(s -> !s.trim().isEmpty()).toArray(String[]::new);
	}

	public double score(KeywordPhrase phrase) {
		DynamicList<String> parts1 = new DynamicList<>(this.parts);
		DynamicList<String> parts2 = new DynamicList<>(phrase.parts);

		double score = 0;
		while (parts1.size() > 0 && parts2.size() > 0) {
			TupleR<Integer[], Double> bestMatch = bestMatchIndexScore(parts1, parts2);
			if (bestMatch.e1 == 0)
				break;
			parts1.remove(bestMatch.e0[0].intValue());
			parts2.remove(bestMatch.e0[1].intValue());
			score += bestMatch.e1;
		}
		return score;
	}

	@SuppressWarnings("deprecation")
	private TupleR<Integer[], Double> bestMatchIndexScore(DynamicList<String> parts1,
		DynamicList<String> parts2) {
		int index1 = 0;
		int index2 = 0;
		double score = 0;
		for (int i = 0; i < parts1.size(); i++) {
			for (int j = 0; j < parts2.size(); j++) {
				String part1 = parts1.get(i);
				String part2 = parts2.get(j);
				double currentScore = StringUtils.getFuzzyDistance(part1, part2, Locale
					.getDefault());
				currentScore /= Math.max(part1.length(), part2.length());
				if (currentScore > score) {
					score = currentScore;
					index1 = i;
					index2 = j;
				}
			}
		}
		// System.out.println("check: " + parts1 + ", " + parts2);
		// System.out.println("best: " + parts1.get(index1) + ", " +
		// parts2.get(index2) + ", "
		// + score);
		Integer[] x = new Integer[]{index1, index2};
		return new TupleR<>(x, score);
	}

	@Override
	public String toString() {
		return String.join(", ", this.parts);
	}
}
