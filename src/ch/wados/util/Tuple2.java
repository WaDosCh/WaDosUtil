package ch.wados.util;

import java.util.Objects;

/**
 * Immutable generic 2-Tuple
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 * @param <T1>
 *            the type of the first element
 * @param <T2>
 *            the type or the second element
 * @deprecated use ch.awae.utils.functional.T2 from awaeUtils
 */
@Deprecated
public class Tuple2<T1, T2> {

	/**
	 * The first element
	 */
	public final T1 _1;
	/**
	 * The second element
	 */
	public final T2 _2;

	/**
	 * Creates a new tuple instance
	 * 
	 * @param _1
	 *            the first element
	 * @param _2
	 *            the second element
	 */
	public Tuple2(T1 _1, T2 _2) {
		this._1 = _1;
		this._2 = _2;
	}

	/**
	 * Returns a tuple with the same elements as this one, but in the reverse
	 * order
	 * 
	 * @return a reversed version of this tuple
	 */
	public Tuple2<T2, T1> flip() {
		return new Tuple2<>(this._2, this._1);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Tuple2))
			return false;
		Tuple2<?, ?> other = (Tuple2<?, ?>) obj;
		return Objects.equals(this._1, other._1) && Objects.equals(this._2, other._2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this._1, this._2);
	}

	@Override
	public String toString() {
		return "(" + this._1 + ";" + this._2 + ")";
	}
}
