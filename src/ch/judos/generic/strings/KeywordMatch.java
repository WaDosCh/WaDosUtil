package ch.judos.generic.strings;

import java.util.Arrays;
import java.util.function.Function;

import ch.judos.generic.data.TupleR;

public class KeywordMatch<T> {

	private TupleR<Integer, Double>[] matches;
	private T[] objects;
	private String search;
	private Function<T, String> toString;

	public KeywordMatch(String search, TupleR<Integer, Double>[] matches, T[] objects,
		Function<T, String> toString) {
		this.search = search;
		this.matches = matches;
		this.objects = objects;
		this.toString = toString;
	}

	/**
	 * @return the string which matched best. This doesn't mean the matcher is
	 *         certain about this match.
	 */
	public T getBestMatch() {
		int bestIndex = this.matches[0].e0;
		return this.objects[bestIndex];
	}

	public String getBestMatchString() {
		return this.toString.apply(getBestMatch());
	}

	public int getBestMatchIndex() {
		return this.matches[0].e0;
	}
	/**
	 * @return probability between 0 and 1 that this match is actually correct
	 */
	public double getCertaintyOfMatch() {
		if (this.matches.length == 0)
			return 0;
		if (this.matches.length == 1) {
			return Math.min(1, this.matches[0].e1 / 2);
		}
		double bestDistance = this.matches[0].e1 - this.matches[1].e1;
		double certainty = 0.559201 * Math.log(10 * bestDistance);
		certainty = Math.min(1, Math.max(0, certainty));
		if (this.matches[0].e1 < 1.4) {
			certainty *= (this.matches[0].e1 / 1.4);
		}
		return certainty;
	}

	/**
	 * @return only returns an object if the matcher is 75% sure about this
	 */
	public T getMatch() {
		if (getCertaintyOfMatch() >= 0.75)
			return getBestMatch();
		return null;
	}

	/**
	 * @return only returns a string if the matcher is 75% sure about this
	 */
	public String getMatchString() {
		if (getCertaintyOfMatch() >= 0.75)
			return getBestMatchString();
		return null;
	}

	public int getMatchIndex() {
		if (getCertaintyOfMatch() >= 0.75)
			return getBestMatchIndex();
		return -1;
	}

	/**
	 * position 0 will return the index of the best guess. Position 1 will
	 * return the second best guess index. And so on...
	 * 
	 * @param position
	 * @return
	 */
	public int getGuessIndexPosition(int position) {
		return this.matches[position].e0;
	}

	public T getGuessObjectPosition(int position) {
		return this.objects[getGuessIndexPosition(position)];
	}

	public String getGuessStringPosition(int position) {
		return this.toString.apply(this.objects[getGuessIndexPosition(position)]);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		Object[] best = null;
		if (this.objects.length >= 3) {
			best = new Object[]{getGuessObjectPosition(0), getGuessObjectPosition(1),
				getGuessObjectPosition(2)};
		}
		else if (this.objects.length == 2) {
			best = new Object[]{getGuessObjectPosition(0), getGuessObjectPosition(1)};
		}
		else if (this.objects.length == 1) {
			best = new Object[]{getGuessObjectPosition(0)};
		}
		else {
			best = new Object[]{"No objects provided"};
		}
		return "search :" + this.search + "\nfound: " + Arrays.toString(best) + "\nCertainty: "
			+ this.getCertaintyOfMatch();
	}

	public int getSize() {
		return this.matches.length;
	}
}
