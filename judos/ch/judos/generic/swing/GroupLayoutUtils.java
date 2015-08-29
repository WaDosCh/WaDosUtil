package ch.judos.generic.swing;

import java.awt.Component;
import java.util.Stack;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;

/**
 * @since 29.12.2014
 * @author Julian Schelker
 */
public class GroupLayoutUtils {
	private GroupLayout l;
	private Stack<Group> groups;

	public GroupLayoutUtils(GroupLayout layout) {
		this.l = layout;
		this.groups = new Stack<>();
	}

	/**
	 * puts a sequential group onto the stack where the given components are
	 * added
	 * 
	 * @param components
	 * @return
	 */
	public GroupLayoutUtils seqIn(Component... components) {
		Group g = this.l.createSequentialGroup();
		for (Component com : components)
			g.addComponent(com);
		if (this.groups.size() > 0)
			this.groups.peek().addGroup(g);
		this.groups.push(g);
		return this;
	}

	/**
	 * puts a parallel group onto the stack where the given components are added
	 * 
	 * @param components
	 * @return
	 */
	public GroupLayoutUtils parIn(Component... components) {
		Group g = this.l.createParallelGroup();
		for (Component com : components)
			g.addComponent(com);
		if (this.groups.size() > 0)
			this.groups.peek().addGroup(g);
		this.groups.push(g);
		return this;
	}

	/**
	 * adds a parallel group with the given components to the group on top of
	 * the stack
	 * 
	 * @param components
	 * @return
	 */
	public GroupLayoutUtils par(Component... components) {
		parIn(components);
		return out();
	}

	/**
	 * adds a sequential group with the given components to the group on top of
	 * the stack
	 * 
	 * @param components
	 * @return
	 */
	public GroupLayoutUtils seq(Component... components) {
		seqIn(components);
		return out();
	}

	/**
	 * adds these components to the group on top of the stack
	 * 
	 * @param components
	 * @return
	 */
	public GroupLayoutUtils add(Component... components) {
		if (this.groups.size() == 0)
			throw new IllegalStateException("Must have a group on the stack first.");
		for (Component com : components)
			this.groups.peek().addComponent(com);
		return this;
	}

	/**
	 * removes the last group which was put on the stack
	 * 
	 * @return
	 */
	public GroupLayoutUtils out() {
		this.groups.pop();
		return this;
	}

	/**
	 * pops and returns the last group which was on the stack
	 * 
	 * @return
	 */
	public Group end() {
		if (this.groups.size() != 1)
			throw new IllegalStateException(
				"Must have exactly one group on the stack to use this method.");
		return this.groups.pop();
	}

	public Group getGroupAndReset() {
		Group result = this.groups.firstElement();
		this.groups.clear();
		return result;
	}

}
