package ch.waan.game.ui;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A container that may have its position altered by its enclosing container.
 * 
 * <p>
 * The implementation rules are the same as for {@link UIMovable}.
 * </p>
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-23
 * 
 * @see UIMovable
 * 
 * @param <E>
 *            The component type allowed in this container
 */
public interface UIMovableContainer<@NonNull E extends UIComponent> extends UIContainer<E>,
	UIMovable {
	// default : no content - pure combination interface
}