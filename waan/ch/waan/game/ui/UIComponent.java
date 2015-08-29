package ch.waan.game.ui;

import java.awt.Graphics2D;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.waan.game.Dimension;
import ch.waan.game.MouseButton;
import ch.waan.game.Point;

/**
 * A basic UI Component.
 * 
 * <p>
 * The implementation of this interface may be very flexible, is however subject
 * to a few rules. It is recommended to follow these rules in any implementation
 * to ensure correct behaviour. The rules are the following:
 * </p>
 * <ul>
 * <li>The position may be changed at any time. Any such change will be applied
 * at the next graphics tick.
 * <li>The dimension should not be changed once the component has been added to
 * a container. Some containers may rely on the dimension of their child
 * components for proper operation.
 * <li>A component will be notified about any relevant changes in the parent
 * container. This includes adding or removing the component to the parent, but
 * also changes in the size of the parent.
 * <li>A parent may perform collision checks at any time. Collision checks are
 * used to determine whether or not the mouse is currently "on" the component.
 * <li>Obsolete components should always be removed from their parent container,
 * since the retained references will prevent garbage collection and increase
 * rendering times.
 * </ul>
 * 
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-22
 */
public interface UIComponent {

	/**
	 * Returns the position of the centre of the component.
	 * 
	 * @return the component position. May <i>never</i> be null
	 */
	public @NonNull Point getPosition();

	/**
	 * Returns the dimension of the component.
	 * 
	 * While the component is a member of a container, this should always yield
	 * the same value. As a sole exception to this rule can be seen dimension
	 * changes in response to a parent update.
	 * 
	 * @return the component dimension. May <i>never</i> be null
	 */
	public @NonNull Dimension getDimension();

	/**
	 * Renders the component.
	 * <p>
	 * The provided {@link Graphics2D graphics} instance may <i>not</i> be
	 * reused after returning from the method. The graphics instance may be
	 * transformed freely, the clip bounds however should not be altered.
	 * Alterations may cause undesired component overlaps and unwanted rendering
	 * artifacts.
	 * </p>
	 * 
	 * @param g
	 *            the {@link Graphics2D graphics} instance to render onto. The
	 *            graphics instance must be centred on the component.
	 */
	public void render(@NonNull Graphics2D g);

	/**
	 * Checks if a given point lies inside the component.
	 * 
	 * @param point
	 *            the point to check the collision for. The point is given
	 *            relative to the centre of the component.
	 * 
	 * @return {@code true} if the given point lies "inside" the component.
	 * 
	 * @implSpec no implementation should extend the collision bounds beyond the
	 *           dimension rectangle. The simplest way to ensure this is to
	 *           always check with the {@code super} method first.
	 * @default checks if the point lies inside the rectangle given by the
	 *          dimension.
	 */
	public default boolean collides(final @NonNull Point point) {
		final Dimension dim = getDimension();
		final Point tl = dim.topLeft();
		final Point br = dim.bottomRight();
		return point.x() > tl.x() && point.x() < br.x() && point.y() > tl.y()
			&& point.y() < br.y();
	}

	// == MOUSE HANDLING

	/**
	 * Notifies the component that the mouse has entered it.
	 * 
	 * @default no action is taken
	 */
	public default void mouseEntered() {
		// default: no action
	}

	/**
	 * Notifies the component that the mouse has left it.
	 * 
	 * @default no action is taken
	 */
	public default void mouseLeft() {
		// default: no action
	}

	/**
	 * Notifies the component about any mouse movement within the component.
	 * 
	 * Mouse movement outside the component is ignored and does not lead to an
	 * invocation of this method. When the mouse enters the component this
	 * method will be invoked directly after {@link #mouseEntered()}.
	 * 
	 * @param mouse
	 *            the position of the mouse relative to the component centre
	 * @default no action is taken
	 */
	@SuppressWarnings("unused")
	public default void mouseMoved(final @NonNull Point mouse) {
		// default: no action
	}

	/**
	 * Notifies the component about the pressing of a mouse button.
	 * 
	 * Only events within the component will trigger an invocation.
	 * 
	 * @param button
	 *            the pressed button
	 * @default no action is taken
	 */
	@SuppressWarnings("unused")
	public default void mouseButtonPressed(final @NonNull MouseButton button) {
		// default: no action
	}

	/**
	 * Notifies the component about the release of a mouse button.
	 * <p>
	 * A release may be trigger an invocation even when the mouse is not
	 * currently inside the component. When the mouse is inside the component, a
	 * release must trigger the invocation. The invocation must also be
	 * triggered, when the mouse button was pressed while inside the component
	 * and the mouse moved out before releasing. In any other case an invocation
	 * is not mandatory.
	 * </p>
	 * 
	 * @param button
	 *            the released button
	 * @default no action is taken
	 */
	@SuppressWarnings("unused")
	public default void mouseButtonReleased(final @NonNull MouseButton button) {
		// default : no action
	}

	/**
	 * Notifies the component about the scrolling of the mouse wheel.
	 * <p>
	 * This notification must <i>only</i> be invoked when the mouse lies inside
	 * the component. All other scroll events must be ignored.
	 * </p>
	 * 
	 * @param scrollSpeed
	 *            the amount the mouse wheel was scrolled by
	 * @default no action is taken
	 */
	@SuppressWarnings("unused")
	public default void mouseWheelScrolled(final double scrollSpeed) {
		// default : no action
	}

	/**
	 * Notifies the component that the parent was updated. If the parent update
	 * represents the removal of the component from its former parent, a
	 * {@code null} value will be passed.
	 * 
	 * @param parent
	 *            the parent container
	 * @default no action is taken
	 */
	@SuppressWarnings("unused")
	public default void
		parentUpdated(final @Nullable UIContainer<? extends UIComponent> parent) {
		// default : no action
	}
}
