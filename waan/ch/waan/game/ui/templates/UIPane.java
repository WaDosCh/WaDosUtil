package ch.waan.game.ui.templates;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.waan.game.Dimension;
import ch.waan.game.MouseButton;
import ch.waan.game.Point;
import ch.waan.game.ui.UIComponent;
import ch.waan.game.ui.UIResizableContainer;

/**
 * A basic UI container.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-23
 */
public class UIPane implements UIResizableContainer<UIComponent> {

	private @NonNull Point position;
	private @NonNull Dimension dimension;
	private @Nullable UIComponent mouseOwner = null;
	private final List<UIComponent> components = new ArrayList<>();

	/**
	 * Creates a new Pane instance
	 * 
	 * @param position
	 *            the position of the pane
	 * @param dimension
	 *            the dimension of the pane
	 */
	public UIPane(final @NonNull Point position, final @NonNull Dimension dimension) {
		this.position = position;
		this.dimension = dimension;
	}

	@Override
	public Point getPosition() {
		return this.position;
	}

	@Override
	public void setPosition(final Point position) {
		assert position != null;
		this.position = position;
	}

	@Override
	public Dimension getDimension() {
		return this.dimension;
	}

	@Override
	public void setDimension(final Dimension dimension) {
		assert dimension != null;
		if (this.dimension.equals(dimension))
			return;
		this.dimension = dimension;
		for (UIComponent c : this.components)
			c.parentUpdated(this);
	}

	@Override
	public void addComponent(final UIComponent component) {
		if (this.components.contains(component))
			this.components.remove(component);
		this.components.add(component);
		component.parentUpdated(this);
		// invariant check (at most one occurrence of a component)
		if (this.components.indexOf(component) != this.components.lastIndexOf(component))
			throw new ConcurrentModificationException();
	}

	@Override
	public void removeComponent(final UIComponent component) {
		this.components.remove(component);
		if (this.mouseOwner == component) {
			component.mouseLeft();
			this.mouseOwner = null;
		}
		component.parentUpdated(null);
	}

	@Override
	public List<UIComponent> getComponents() {
		List<UIComponent> list = Collections.unmodifiableList(this.components);
		if (list == null)
			throw new AssertionError(); // should never happen
		return list;
	}

	@Override
	public void render(final Graphics2D g) {
		for (UIComponent c : this.components) {
			final Dimension d = c.getDimension();
			final Point p = c.getPosition();
			// perform clip check
			if (!this.isVisibleOnScreen(c))
				continue;
			// draw component
			final Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(p.x(), p.y());
			g2.clipRect(d.topLeft().x(), d.topLeft().y(), d.width() + 1, d.height() + 1);
			c.render(g2);
		}
	}

	/**
	 * Checks if a given component is visible in the visible area of the pane
	 * 
	 * @param comp
	 *            the component to check for
	 * @return {@code true} if any part of the component <i>might</i> be visible
	 * 
	 * @default simple bounding rectangle collision check
	 */
	protected boolean isVisibleOnScreen(final @NonNull UIComponent comp) {
		int dx = comp.getPosition().x();
		int dy = comp.getPosition().y();
		dx = dx < 0 ? -dx : dx;
		dy = dy < 0 ? -dy : dy;
		return !(2 * dx > comp.getDimension().width() + this.dimension.width() || 2 * dy > comp
			.getDimension().height()
			+ this.dimension.height());
	}

	@Override
	public boolean collides(final Point point) {
		assert point != null;
		if (!UIResizableContainer.super.collides(point))
			return false;
		return hasChildCollision(point);
	}

	/**
	 * Checks if any child collides with the given point
	 * 
	 * @param point
	 *            the point to check for collision for
	 * @return {@code true} if a child collides with the point
	 * @default looks through all child components and performs collision checks
	 *          on their bounding rectangles
	 */
	protected boolean hasChildCollision(final @NonNull Point point) {
		for (UIComponent c : this.components) {
			if (c.collides(point.translate(c.getPosition().scale(-1))))
				return true;
		}
		return false;
	}

	/**
	 * Returns the child that collides with the given point or {@code null} if
	 * no such child exists.
	 * 
	 * @param point
	 *            the point to get the front-most child for
	 * @return the colliding child
	 */
	protected @Nullable UIComponent childAt(final @NonNull Point point) {
		UIComponent child = null;
		for (UIComponent c : this.components)
			if (c.collides(point.translate(c.getPosition().scale(-1))))
				child = c;
		return child;
	}

	// MOUSE MANAGEMENT

	@Override
	public void mouseMoved(Point mouse) {
		assert mouse != null;
		UIComponent newOwner = this.childAt(mouse);
		UIComponent oldOwner = this.mouseOwner;
		// perform potential switch
		if (newOwner != oldOwner) {
			if (oldOwner != null) {
				oldOwner.mouseLeft();
			}
			if (newOwner != null)
				newOwner.mouseEntered();
		}
		// apply movement
		if (newOwner != null)
			newOwner.mouseMoved(mouse.translate(newOwner.getPosition().scale(-1)));
		// store new owner
		this.mouseOwner = newOwner;
	}

	@Override
	public void mouseLeft() {
		if (this.mouseOwner != null) {
			this.mouseOwner.mouseLeft();
		}
		this.mouseOwner = null;
	}

	@Override
	public void mouseButtonPressed(final MouseButton button) {
		if (this.mouseOwner != null)
			this.mouseOwner.mouseButtonPressed(button);
	}

	@Override
	public void mouseButtonReleased(final MouseButton button) {
		for (UIComponent c : this.components)
			c.mouseButtonReleased(button);
	}

	@Override
	public void mouseWheelScrolled(double scrollSpeed) {
		if (this.mouseOwner != null)
			this.mouseOwner.mouseWheelScrolled(scrollSpeed);
	}

}
