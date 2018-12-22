package ch.judos.generic.gui;

/**
 * @since 20.09.2014
 * @author Julian Schelker
 * @param <T>
 *            data object
 */
public interface ComponentListSelectionListener<T> {

	public void valueSelectionChanged(ComponentListEntry visible, T data);
}
