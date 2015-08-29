package ch.judos.generic.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * implements the focus traveral policy as a list
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public class FocusTraversalPolicyList extends FocusTraversalPolicy {

	/**
	 * the components and their order stored in a list
	 */
	public List<Component> list;

	/**
	 * creates a policy with no components
	 */
	public FocusTraversalPolicyList() {
		this.list = new ArrayList<>();
	}

	/**
	 * creates a policy with a list of components
	 * 
	 * @param order
	 */
	public FocusTraversalPolicyList(List<Component> order) {
		this.list = order;
	}

	/**
	 * creates a policy with an array of components
	 * 
	 * @param order
	 */
	public FocusTraversalPolicyList(Component[] order) {
		this.list = new ArrayList<>();
		for (int i = 0; i < order.length; i++)
			this.list.add(order[i]);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.FocusTraversalPolicy#getComponentAfter(java.awt.Container,
	 *      java.awt.Component)
	 */
	@Override
	public Component getComponentAfter(Container container, Component component) {
		int index = this.list.indexOf(component);
		return this.list.get((index + 1) % this.list.size());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.FocusTraversalPolicy#getComponentBefore(java.awt.Container,
	 *      java.awt.Component)
	 */
	@Override
	public Component getComponentBefore(Container container, Component component) {
		int index = this.list.indexOf(component);
		return this.list.get((index - 1 + this.list.size()) % this.list.size());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.FocusTraversalPolicy#getDefaultComponent(java.awt.Container)
	 */
	@Override
	public Component getDefaultComponent(Container container) {
		return this.list.get(0);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.FocusTraversalPolicy#getFirstComponent(java.awt.Container)
	 */
	@Override
	public Component getFirstComponent(Container container) {
		return this.list.get(0);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.FocusTraversalPolicy#getLastComponent(java.awt.Container)
	 */
	@Override
	public Component getLastComponent(Container container) {
		return this.list.get(this.list.size() - 1);
	}

}
