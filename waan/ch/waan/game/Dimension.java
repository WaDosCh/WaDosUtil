package ch.waan.game;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The Dimension of a UI Component.
 * 
 * <p>
 * UI measurements are taken from the centre of the component. To facilitate
 * usage of the dimension data, helper methods are provided to directly retrieve
 * the point coordinates of the corners. These corner calculations are performed
 * under the assumptions that the centre of the component is located at (0,0).
 * Therefore by translating a corner by the component centre one can very easily
 * retrieve the actual corner position.
 * </p>
 * 
 * <p>
 * This value class is immutable, the instances are however <i>not</i> managed.
 * Therefore content equality checks can <i>not</i> be done using instance
 * equality.
 * </p>
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-22
 */
public final class Dimension {

	// STATIC FACTORY

	/**
	 * Creates a new Dimension instance with the given measurements
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return a new dimension with the given measurements
	 * @throws IllegalArgumentException
	 *             if any parameter is negative
	 */
	public static @NonNull Dimension get(final int width, final int height) {
		if (width < 0 || height < 0)
			throw new IllegalArgumentException(
					"Dimension components must be positive. given pair was: ("
							+ width + ", " + height + ")");
		return new Dimension(width, height);
	}

	// INSTANCE STUFF

	private final int	width, height;

	private Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the width of this dimension
	 * 
	 * @return the width
	 */
	public int width() {
		return this.width;
	}

	/**
	 * Returns the height of this dimension
	 * 
	 * @return the height
	 */
	public int height() {
		return this.height;
	}

	private Point	topLeft, topRight, bottomLeft, bottomRight;

	/**
	 * Returns the top-left corner.
	 * 
	 * @return the corner to the top left.
	 */
	// lazy init is null-safe
	public @NonNull Point topLeft() {
		if (this.topLeft == null)
			return this.topLeft = Point.get(-this.width / 2, -this.height / 2);
		return this.topLeft;
	}

	/**
	 * Returns the top-right corner.
	 * 
	 * @return the corner to the top right.
	 */
	// lazy init is null-safe
	public @NonNull Point topRight() {
		if (this.topRight == null)
			this.topRight = Point.get(this.width / 2, -this.height / 2);
		return this.topRight;
	}

	/**
	 * Returns the bottom-left corner.
	 * 
	 * @return the corner to the bottom left.
	 */
	// lazy init is null-safe
	public @NonNull Point bottomLeft() {
		if (this.bottomLeft == null)
			this.bottomLeft = Point.get(-this.width / 2, this.height / 2);
		return this.bottomLeft;
	}

	/**
	 * Returns the bottom-right corner.
	 * 
	 * @return the corner to the bottom right.
	 */
	// lazy init is null-safe
	public @NonNull Point bottomRight() {
		if (this.bottomRight == null)
			this.bottomRight = Point.get(this.width / 2, this.height / 2);
		return this.bottomRight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.height;
		result = prime * result + this.width;
		return result;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (!(obj instanceof Dimension))
			return false;
		Dimension other = (Dimension) obj;
		if (this.height != other.height)
			return false;
		if (this.width != other.width)
			return false;
		return true;
	}

	@Override
	public @NonNull String toString() {
		return "Dimension (" + this.width + " x " + this.height + ")";
	}
}
