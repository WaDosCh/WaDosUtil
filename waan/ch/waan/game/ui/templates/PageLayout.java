/*
 * AwaeGameAPI - easy to use 2D game API Copyright (C) 2015 Andreas Waelchli
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.waan.game.ui.templates;

import java.awt.Graphics2D;

import org.eclipse.jdt.annotation.NonNull;

import ch.waan.game.Dimension;
import ch.waan.game.MouseButton;
import ch.waan.game.Point;
import ch.waan.game.ui.UIComponent;
import ch.waan.game.ui.UIContainer;

/**
 * A Page layout.
 * 
 * <p>
 * The page layout template is specifically designed for the use as a high-level
 * container. In most cases it will be the sole component in the root container
 * of the GUI.
 * </p>
 * <p>
 * A page layout is a sub-pane organisation, where a fixed-height header and
 * footer are present. The remaining space in between is horizontally shared
 * between a fixed-width left side panel, a fixed-width right side panel and -
 * occupying the remaining space in between those - the main pane. The page
 * layout automatically fills it's parent container to the full extent and
 * arranges the sub-panes according to the described rules. It is to be noted
 * that - in compliance with the definitions for the interpretation of component
 * positioning and sizing the coordinate system of all sub-panes are centred at
 * the corresponding sub-pane. Hence the vertical centre of the main pane is not
 * in the centre of the parent container, but rather half-way between the lower
 * edge of the header and the upper edge of the footer. The same basic rule also
 * applies to the vertical positioning of the side panes and to the horizontal
 * positioning of the main pane.
 * </p>
 * <p>
 * While the page layout technically is a component that emulates container
 * behaviour through the use of multiple sub-components, it can - from a design
 * point of view - be seen as a logical container. From a structural point of
 * view however the internal sub-pane management breaks the normal UI hierarchy.
 * Each sub-pane basically forms a root container of its own that is
 * structurally independent from the rest of the GUI system. Due to the internal
 * emulation of container-like behaviour this can however be ignored for most
 * practical applications.
 * </p>
 * <p>
 * Note also that the page layout possesses solid collision bounds. I.e.
 * components behind a page layout will never be able to gain mouse focus - even
 * if no sub-component of the page layout covers it.
 * </p>
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-03-08
 */
public final class PageLayout implements UIComponent {

	private int head, foot, left, right;

	private @NonNull Dimension dimension;
	private final @NonNull Point position;

	private final @NonNull UIPane assistLayer, header, footer, leftSide, rightSide, mainPane;

	/**
	 * Creates a new page layout instance.
	 * 
	 * The component will initially have the dimensions (0, 0). This value will
	 * however be adjusted as soon as the component is added to a container.
	 * 
	 * @param head
	 *            the initial header height
	 * @param foot
	 *            the initial footer height
	 * @param left
	 *            the initial left side pane width
	 * @param right
	 *            the initial right side pane width
	 * @throws IllegalArgumentException
	 *             if any parameter is negative
	 */
	public PageLayout(final int head, final int foot, final int left, final int right) {
		if (head < 0 || foot < 0 || left < 0 || right < 0)
			throw new IllegalArgumentException(
				"all arguments must be positive. values were: (" + head + ", " + foot + ", "
					+ left + ", " + right + ")");
		final Point ZERO = Point.get(0, 0);
		final Dimension TEMP_D = Dimension.get(0, 0);
		this.head = head;
		this.foot = foot;
		this.left = left;
		this.right = right;
		this.position = ZERO;
		this.dimension = Dimension.get(left + right, head + foot);
		this.assistLayer = new UIPane(ZERO, TEMP_D);
		this.header = new UIPane(ZERO, TEMP_D);
		this.footer = new UIPane(ZERO, TEMP_D);
		this.leftSide = new UIPane(ZERO, TEMP_D);
		this.rightSide = new UIPane(ZERO, TEMP_D);
		this.mainPane = new UIPane(ZERO, TEMP_D);
		this.assistLayer.addComponent(this.header);
		this.assistLayer.addComponent(this.footer);
		this.assistLayer.addComponent(this.leftSide);
		this.assistLayer.addComponent(this.rightSide);
		this.assistLayer.addComponent(this.mainPane);
		this.updateComponents();
	}

	@Override
	public @NonNull Dimension getDimension() {
		return this.dimension;
	}

	/**
	 * Returns the footer pane
	 * 
	 * @return the footer
	 */
	public UIContainer<UIComponent> getFooter() {
		return this.footer;
	}

	/**
	 * Returns the header pane
	 * 
	 * @return the header
	 */
	public UIContainer<UIComponent> getHeader() {
		return this.header;
	}

	/**
	 * Returns the left side pane
	 * 
	 * @return the leftSide
	 */
	public UIContainer<UIComponent> getLeftSide() {
		return this.leftSide;
	}

	/**
	 * Returns the main pane
	 * 
	 * @return the mainPane
	 */
	public UIContainer<UIComponent> getMainPane() {
		return this.mainPane;
	}

	@Override
	public @NonNull Point getPosition() {
		return this.position;
	}

	/**
	 * Returns the right side pane
	 * 
	 * @return the rightSide
	 */
	public UIContainer<UIComponent> getRightSide() {
		return this.rightSide;
	}

	@Override
	public void mouseButtonPressed(final MouseButton button) {
		this.assistLayer.mouseButtonPressed(button);
	}

	@Override
	public void mouseButtonReleased(final MouseButton button) {
		this.assistLayer.mouseButtonReleased(button);
	}

	@Override
	public void mouseLeft() {
		this.assistLayer.mouseLeft();
	}

	@Override
	public void mouseMoved(final Point mouse) {
		this.assistLayer.mouseMoved(mouse);
	}

	@Override
	public void mouseWheelScrolled(final double scrollSpeed) {
		this.assistLayer.mouseWheelScrolled(scrollSpeed);
	}

	@Override
	public void parentUpdated(final UIContainer<? extends UIComponent> parent) {
		if (parent != null) {
			this.dimension = parent.getDimension();
			this.updateComponents();
		}
	}

	@Override
	public void render(final Graphics2D g) {
		this.assistLayer.render(g);
	}

	/**
	 * Sets the footer pane height.
	 * 
	 * @param foot
	 *            the new footer height
	 * @throws IllegalArgumentException
	 *             if the given height is negative
	 */
	public void setFoot(final int foot) {
		if (foot < 0)
			throw new IllegalArgumentException(
				"foot height may not be negative. received value " + foot);
		this.foot = foot;
		this.updateComponents();
	}

	/**
	 * Sets the head pane height.
	 * 
	 * @param head
	 *            the new head height
	 * @throws IllegalArgumentException
	 *             if the given height is negative
	 */
	public void setHead(final int head) {
		if (head < 0)
			throw new IllegalArgumentException(
				"head height may not be negative. received value " + head);
		this.head = head;
		this.updateComponents();
	}

	/**
	 * Sets the left side pane width.
	 * 
	 * @param left
	 *            the new left side pane width
	 * @throws IllegalArgumentException
	 *             if the given width is negative
	 */
	public void setLeft(final int left) {
		if (left < 0)
			throw new IllegalArgumentException(
				"left side width may not be negative. received value " + left);
		this.left = left;
		this.updateComponents();
	}

	/**
	 * Sets the right side pane width.
	 * 
	 * @param right
	 *            the new right side pane width
	 * @throws IllegalArgumentException
	 *             if the given width is negative
	 */
	public void setRight(final int right) {
		if (right < 0)
			throw new IllegalArgumentException(
				"right side width may not be negative. received value " + right);
		this.right = right;
		this.updateComponents();
	}

	/**
	 * updates the component sizes
	 */
	private void updateComponents() {
		final int pageW = Math.max(this.dimension.width() - this.left - this.right, 0);
		final int pageH = Math.max(this.dimension.height() - this.head - this.foot, 0);
		this.assistLayer.setDimension(this.dimension);
		// header
		this.header.setDimension(Dimension.get(this.dimension.width(), this.head));
		this.header.setPosition(Point.get(0, (this.head - this.dimension.height()) / 2));
		// footer
		this.footer.setDimension(Dimension.get(this.dimension.width(), this.foot));
		this.footer.setPosition(Point.get(0, (this.dimension.height() - this.foot) / 2));
		// left side
		this.leftSide.setDimension(Dimension.get(this.left, pageH));
		this.leftSide.setPosition(Point.get((this.left - this.dimension.width()) / 2,
			(this.head - this.foot) / 2));
		// right side
		this.rightSide.setDimension(Dimension.get(this.right, pageH));
		this.rightSide.setPosition(Point.get((this.dimension.width() - this.right) / 2,
			(this.head - this.foot) / 2));
		// main pane
		this.mainPane.setDimension(Dimension.get(pageW, pageH));
		this.mainPane.setPosition(Point.get((this.left - this.right) / 2,
			(this.head - this.foot) / 2));
	}

}
