package ch.waan.game.ui.templates;

import org.eclipse.jdt.annotation.NonNull;

import ch.waan.game.Dimension;
import ch.waan.game.Point;
import ch.waan.game.ui.UIMovable;

/**
 * A simple fixed-size canvas that can be used for basic sprite rendering or as
 * a base for more complex operations and IO interactions.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-23
 */
public abstract class UICanvas implements UIMovable {

	private @NonNull Point position;
	private final @NonNull Dimension dimension;

	/**
	 * Creates a new canvas instance.
	 * 
	 * @param position
	 *            the position of the canvas
	 * @param dimension
	 *            the dimension of the canvas
	 */
	public UICanvas(final @NonNull Point position, final @NonNull Dimension dimension) {
		this.position = position;
		this.dimension = dimension;
	}

	@Override
	public final @NonNull Dimension getDimension() {
		return this.dimension;
	}

	@Override
	public final @NonNull Point getPosition() {
		return this.position;
	}

	@Override
	public final void setPosition(final Point position) {
		assert position != null;
		this.position = position;
	}

}
