/*
 * AwaeGameAPI - easy to use 2D game API
 * Copyright (C) 2015 Andreas Waelchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.waan.game.ui.templates;

import java.awt.Graphics2D;

import org.eclipse.jdt.annotation.NonNull;

import ch.waan.game.Dimension;
import ch.waan.game.Point;
import ch.waan.game.ui.UIComponent;

/**
 * A Panel with the ability to zoom and scroll
 * 
 * @author Andreas Waelchli
 * @version 1.2, 2015-03-08
 * @since AwaeGameAPI 0.2 (0.1 without extension)
 */
public final class UIScrollZoomPane extends UIPane {

	private double zoom = 1;

	private @NonNull Point scrollPos = Point.get(0, 0);
	private @NonNull Dimension bodySize;

	/**
	 * Creates a new Zoom Pane.
	 * 
	 * @param position
	 *            the position of the pane
	 * @param dimension
	 *            the dimension of the pane
	 * @param bodySize
	 *            the dimension of the content area. The content area is what is
	 *            zoomed and moved around.
	 */
	public UIScrollZoomPane(final @NonNull Point position, final @NonNull Dimension dimension,
		final @NonNull Dimension bodySize) {
		super(position, dimension);
		this.bodySize = bodySize;
	}

	/**
	 * Moves the scroll panel in the given directions
	 * 
	 * @param dx
	 *            the movement along the x-axis
	 * @param dy
	 *            the movement along the y-axis
	 */
	public void move(final int dx, final int dy) {
		this.scrollPos = this.scrollPos.translate(dx, dy);
		this.updateBounds();
	}

	/**
	 * Returns the current Zoom Level
	 * 
	 * @return the current Zoom level
	 */
	public double getZoom() {
		return this.zoom;
	}

	/**
	 * Sets a new zoom level
	 * 
	 * @param newZoom
	 *            the new zoom level
	 * @throws IllegalArgumentException
	 *             if the given zoom level is zero or negative
	 */
	public void zoom(final double newZoom) {
		if (newZoom <= 0)
			throw new IllegalArgumentException(
				"zoom level may not be zero or negative. recieved value " + newZoom);
		this.zoom = newZoom;
		this.updateBounds();
	}

	@Override
	public void setDimension(Dimension dimension) {
		super.setDimension(dimension);
		this.updateBounds();
	}

	private double enforceZoom(final double targetZoom) {
		return Math.max(targetZoom, Math.max(((double) this.getDimension().width())
			/ this.bodySize.width(), ((double) this.getDimension().height())
			/ this.bodySize.height()));
	}

	private void updateBounds() {
		final double zoomLevel = this.enforceZoom(this.zoom);
		final Dimension dim = this.getDimension();

		// determine scene coordinates for the screen corners
		final Point Pmin = dim.topLeft().scale(1 / zoomLevel).translate(this.scrollPos);
		final Point Pmax = dim.bottomRight().scale(1 / zoomLevel).translate(this.scrollPos);

		// limit to be inside the scene
		final int newX = this.scrollPos.x()
			- ((Pmin.x() < this.bodySize.topLeft().x()) ? (Pmin.x() - this.bodySize.topLeft()
				.x()) : (Pmax.x() > this.bodySize.bottomRight().x())
				? (Pmax.x() - this.bodySize.bottomRight().x()) : 0);
		final int newY = this.scrollPos.y()
			- ((Pmin.y() < this.bodySize.topLeft().y()) ? (Pmin.y() - this.bodySize.topLeft()
				.y()) : (Pmax.y() > this.bodySize.bottomRight().y())
				? (Pmax.y() - this.bodySize.bottomRight().y()) : 0);

		// set new values
		this.scrollPos = Point.get(newX, newY);
		this.zoom = zoomLevel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @implSpec always returns {@code true} (will be changed in the future)
	 */
	@Override
	protected boolean isVisibleOnScreen(@NonNull UIComponent comp) {
		return true;
		/*
		 * int dx = comp.getPosition().x(); int dy = comp.getPosition().y(); dx
		 * = dx < 0 ? -dx : dx; dy = dy < 0 ? -dy : dy; return !(2 * dx >
		 * comp.getDimension().width() + this.getDimension().width() || 2 * dy >
		 * comp.getDimension() .height() + this.getDimension().height());
		 */
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @implSpec always returns {@code true}. This makes the scroll panel
	 *           effectively opaque.
	 */
	@Override
	protected boolean hasChildCollision(final Point point) {
		return true;
	}

	@Override
	public void mouseMoved(final Point mouse) {
		final Point transformed = mouse.scale(1 / this.zoom).translate(this.scrollPos);
		super.mouseMoved(transformed);
	}

	@Override
	public void render(Graphics2D g) {
		g.scale(this.zoom, this.zoom);
		g.translate(-this.scrollPos.x(), -this.scrollPos.y());
		super.render(g);
	}

}
