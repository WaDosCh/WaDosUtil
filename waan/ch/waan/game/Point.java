package ch.waan.game;

import java.util.WeakHashMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A Point on the GUI.
 * 
 * This value class should be used whenever a position in the UI system is to be
 * referenced. Since instances of this class are immutable, instance reuse is
 * enforced by this class. Therefore it is possible to check for content
 * equality through instance reference equality ({@code ==}).
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-22
 */
public final class Point {

	// == STATIC FACTORY METHODS ==

	private static final WeakHashMap<@NonNull Long, @Nullable Point> references = new WeakHashMap<>();

	/**
	 * provides the point instance for the given x and y coordinates.
	 * 
	 * Two subsequent calls to this method with the same arguments are
	 * guaranteed to yield the same instance, unless the first result was
	 * collected by the GC.
	 * 
	 * @param x
	 *            the x coordinate of the point
	 * @param y
	 *            the y coordinate of the point
	 * @return the point instance for the given x and y coordinates
	 */
	public static synchronized @NonNull Point get(int x, int y) {
		Long n = (((long) x) << 32) | (y & 0XFFFFFFFFL);
		Point stored = references.get(n);
		if (stored == null) {
			stored = new Point(x, y);
			references.put(n, stored);
		}
		return stored;
	}

	// == INSTANCE ==

	private final int x, y;

	private Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the value for the X component of the point.
	 * 
	 * @return the value for the Y component
	 */
	public int x() {
		return this.x;
	}

	/**
	 * Returns the value for the Y component of the point.
	 * 
	 * @return the value for the Y component
	 */
	public int y() {
		return this.y;
	}

	/**
	 * Translates this point by the given amounts.
	 * 
	 * @param dx
	 *            the amount the x axis is translated by
	 * @param dy
	 *            the amount the y axis is translated by
	 * @return A Point corresponding to this point translated by the given
	 *         amounts
	 */
	public @NonNull Point translate(int dx, int dy) {
		if (dx == 0 && dy == 0)
			return this;
		return Point.get(this.x + dx, this.y + dy);
	}

	/**
	 * Translates this point by the components of the given point.
	 * 
	 * This is a convenience method for {@code translate(point.x(), point.y())}.
	 * 
	 * @param point
	 *            the point containing the translation distances
	 * @return A new Point corresponding to this point translated by the
	 *         components of the given point.
	 */
	public @NonNull Point translate(@NonNull Point point) {
		return this.translate(point.x, point.y);
	}

	/**
	 * Scales the point by the given factor
	 * 
	 * @param factor
	 *            the factor to scale by
	 * @return A Point corresponding the this point scaled by the given factor
	 */
	public @NonNull Point scale(double factor) {
		if (factor == 1)
			return this;
		return Point.get((int) (this.x * factor), (int) (this.y * factor));
	}

	/**
	 * Returns a new builder based on this point
	 * 
	 * @return the new builder
	 */
	public @NonNull Builder builder() {
		return new Builder(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.y;
		return result;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return this == obj;
	}

	@Override
	public @NonNull String toString() {
		return "Point (" + this.x + "," + this.y + ")";
	}

	/**
	 * calculates the square distance to another point
	 * 
	 * @param other
	 *            the point to calculate the square distance to
	 * @return the square of the distance between this point and the given one
	 */
	public double squareDistanceTo(final @NonNull Point other) {
		final int dx = this.x - other.x;
		final int dy = this.y - other.y;
		return (dx * dx) + (dy * dy);
	}

	/**
	 * A Builder for precise floating-point operations on points.
	 * 
	 * @author Andreas Waelchli
	 * @version 1.1, 2015-02-27
	 * @since Point 1.1 (AwaeGameAPI 0.1)
	 */
	public final static class Builder {

		private double x, y;

		/**
		 * Creates a new Builder for the given point.
		 * 
		 * @param point
		 *            the point to base the builder on
		 */
		public Builder(final @NonNull Point point) {
			this.x = point.x();
			this.y = point.y();
		}

		/**
		 * Returns the point instance that most accurately describes the current
		 * builder state. The internal builder coordinates are rounded according
		 * to the primitive casting rules.
		 * 
		 * @return the point currently described by the builder
		 */
		public @NonNull Point get() {
			return Point.get((int) this.x, (int) this.y);
		}

		/**
		 * Sets the x axis.
		 * 
		 * @param x
		 *            the new x value
		 * @return the builder itself. can be used for chaining
		 */
		public @NonNull Builder setX(double x) {
			this.x = x;
			return this;
		}

		/**
		 * Sets the y axis.
		 * 
		 * @param y
		 *            the new y value
		 * @return the builder itself. can be used for chaining
		 */
		public @NonNull Builder setY(double y) {
			this.y = y;
			return this;
		}

		/**
		 * Translates the point by the given amount
		 * 
		 * @param dx
		 *            the amount to translate the x axis by
		 * @param dy
		 *            the amount to translate the y axis by
		 * @return the builder itself. can be used for chaining
		 */
		public @NonNull Builder translate(double dx, double dy) {
			this.x += dx;
			this.y += dy;
			return this;
		}

		/**
		 * Scales the axes by the given amounts
		 * 
		 * @param xFactor
		 *            the factor to scale the x axis by
		 * @param yFactor
		 *            the factor to scale the y axis by
		 * @return the builder itself. can be used for chaining
		 */
		public @NonNull Builder scale(double xFactor, double yFactor) {
			this.x *= xFactor;
			this.y *= yFactor;
			return this;
		}

		/**
		 * Scales the point by the given amount.
		 * 
		 * This method is equivalent to {@code scale(factor, factor)}
		 * 
		 * @param factor
		 *            the factor to scale by
		 * @return the builder itself. can be used for chaining
		 */
		public @NonNull Builder scale(double factor) {
			return this.scale(factor, factor);
		}

		@Override
		public @NonNull String toString() {
			return "Point Builder ( " + this.x + ", " + this.y + ")";
		}

	}

}
