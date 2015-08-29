package ch.waan.game.ui;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A container that may have its position and dimension altered by its enclosing
 * container.
 * 
 * <p>
 * The implementation rules are the same as for {@link UIResizable}.
 * Additionally any resizing <i>must</i> notify the container children through
 * their {@link UIComponent#parentUpdated(UIContainer) parentUpdated} method.
 * </p>
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-23
 * 
 * @see UIResizable
 * 
 * @param <E>
 *            The component type allowed in this container
 */
public interface UIResizableContainer<@NonNull E extends UIComponent> extends
	UIMovableContainer<E>, UIResizable {
	// no content - pure combo-interface
}
