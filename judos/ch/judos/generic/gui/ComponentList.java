package ch.judos.generic.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;


import ch.judos.generic.data.TupleR;

/**
 * @since 20.09.2014
 * @author Julian Schelker
 * @param <T>
 *           component
 */
public class ComponentList<T> {
	protected JRootPane													contentPanel;
	protected JScrollPane												content;
	protected ArrayList<TupleR<ComponentListEntry, T>>			entries;
	protected int															selectedIndex;
	protected ComponentListEntry										hoveredComponent;

	/**
	 * use as id to show that no entry is selected or found
	 */
	public static final int												NO_ENTRY	= -1;
	protected ArrayList<ComponentListSelectionListener<T>>	selectionListeners;
	protected ArrayList<MouseListener>								mouseListeners;
	protected ComponentList<T>.GlassPaneListener					glassPaneListener;
	/**
	 * if false, content will be aligned top
	 */
	protected boolean														alignCentered;
	protected JPanel														alignementFillerPanel;

	/**
	 * use getContent() to add this componentList to a JFrame or JPanel.<br>
	 * Uses no center alignement. content will be starting from top
	 */
	public ComponentList() {
		this(false);
	}

	/**
	 * use getContent() to add this componentList to a JFrame or JPanel
	 * 
	 * @param alignCentered
	 */
	public ComponentList(boolean alignCentered) {
		this.alignCentered = alignCentered;
		this.contentPanel = new JRootPane();

		this.content = new JScrollPane(this.contentPanel);
		this.content.setBorder(new LineBorder(Color.gray));
		this.entries = new ArrayList<>();
		this.selectedIndex = NO_ENTRY;
		this.hoveredComponent = null;
		this.glassPaneListener = new GlassPaneListener();
		this.selectionListeners = new ArrayList<>();
		this.mouseListeners = new ArrayList<>();

		this.contentPanel.getGlassPane().addMouseListener(this.glassPaneListener);
		this.contentPanel.getGlassPane().addMouseMotionListener(this.glassPaneListener);
		this.contentPanel.getGlassPane().setVisible(true);
		updateContent();
	}

	public void setCenterAligned(boolean alignCentered) {
		this.alignCentered = alignCentered;
		updateContent();
	}

	protected void updateContent() {
		Container con = this.contentPanel.getContentPane();

		con.setLayout(new GridBagLayout());
		con.removeAll();

		GridBagConstraints c = formatLayoutForContent();
		for (int i = 0; i < this.entries.size(); i++) {
			c.gridy = i;
			con.add(this.entries.get(i).e0, c);
		}
		c.gridy++;
		formatAfterContentAdded(con, c);
		con.repaint();
	}

	protected void formatAfterContentAdded(Container con, GridBagConstraints c) {
		if (!this.alignCentered) {
			c.weighty = 100;
			this.alignementFillerPanel = new JPanel();
			con.add(this.alignementFillerPanel, c);
		}
	}

	protected GridBagConstraints formatLayoutForContent() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		if (this.alignCentered)
			c.anchor = GridBagConstraints.CENTER;
		else
			c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0;
		c.weightx = 1;
		return c;
	}

	public void addMouseListener(MouseListener l) {
		if (l == null)
			throw new InvalidParameterException("parameter mustn't be null");
		this.mouseListeners.add(l);
	}

	public void removeMouseListener(MouseListener l) {
		this.mouseListeners.remove(l);
	}

	/**
	 * @param l
	 *           the listener is notified when an entry is selected
	 */
	public void addListSelectionListener(ComponentListSelectionListener<T> l) {
		if (l == null)
			throw new InvalidParameterException("parameter mustn't be null");
		this.selectionListeners.add(l);
	}

	public void removeListSelectionListener(ListSelectionListener l) {
		this.selectionListeners.remove(l);
	}

	/**
	 * adds a list entry to the componentList
	 * 
	 * @param visibleEntry
	 * @param dataEntry
	 */
	public void addEntry(ComponentListEntry visibleEntry, T dataEntry) {
		this.entries.add(new TupleR<>(visibleEntry, dataEntry));

		updateContent();
	}

	public void removeEntry(int index) {
		this.entries.remove(index);

		updateContent();
	}

	public void removeAllEntries() {
		this.entries.clear();
		updateContent();
	}

	/**
	 * @param visibleComponent
	 *           you will get this from your mouseEvent.getComponent()
	 * @return -1 if the element was not found
	 */
	public int getIndexOf(Component visibleComponent) {
		if (visibleComponent == null)
			return NO_ENTRY;
		for (int i = 0; i < this.entries.size(); i++)
			if (visibleComponent == this.entries.get(i).e0)
				return i;
		return NO_ENTRY;
	}

	public T getDataOf(Component visibleComponent) {
		return getDataOf(getIndexOf(visibleComponent));
	}

	public T getDataOf(int index) {
		if (index == NO_ENTRY)
			return null;
		return this.entries.get(index).e1;
	}

	public int getIndexOf(T data) {
		for (int i = 0; i < this.entries.size(); i++)
			if (data == this.entries.get(i).e1)
				return i;
		return NO_ENTRY;
	}

	public Component getVisualOf(T data) {
		return getVisualOf(getIndexOf(data));
	}

	public Component getVisualOf(int index) {
		if (index == NO_ENTRY)
			return null;
		return this.entries.get(index).e0;
	}

	/**
	 * <b>Note:</b> this method does not notify any ListSelectionListeners about
	 * the change. Use selectAndNotifyListeners(Component) if you want to let
	 * your listeners know about the change.
	 * 
	 * @param visibleComponent
	 */
	public void select(Component visibleComponent) {
		if (this.selectedIndex != NO_ENTRY)
			this.entries.get(this.selectedIndex).e0.setSelected(false);

		this.selectedIndex = getIndexOf(visibleComponent);
		if (this.selectedIndex != NO_ENTRY)
			this.entries.get(this.selectedIndex).e0.setSelected(true);
	}

	public void selectAndNotifyListeners(Component component) {
		select(component);
		for (ComponentListSelectionListener<T> l : this.selectionListeners)
			l.valueSelectionChanged(getSelectedVisible(), getSelectedData());
	}

	public ComponentListEntry getSelectedVisible() {
		if (this.selectedIndex == NO_ENTRY)
			return null;
		return this.entries.get(this.selectedIndex).e0;
	}

	public T getSelectedData() {
		if (this.selectedIndex == NO_ENTRY)
			return null;
		return this.entries.get(this.selectedIndex).e1;
	}

	public TupleR<ComponentListEntry, T> getSelected() {
		if (this.selectedIndex == NO_ENTRY)
			return null;
		return this.entries.get(this.selectedIndex);
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	/**
	 * this is the content pane that you can add to your jframe / jpanel
	 * 
	 * @return
	 */
	public Component getContent() {
		return this.content;
	}

	private class GlassPaneListener implements MouseInputListener {

		/**
		 * used to generated mouseExited and mouseEntered events
		 */
		private Component				currentlyHovered;
		private ComponentListEntry	currentEntryHovered;
		private ComponentList<T>	that	= ComponentList.this;
		Component						glass	= ComponentList.this.contentPanel.getGlassPane();
		Container						con	= ComponentList.this.contentPanel.getContentPane();

		public GlassPaneListener() {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			ComponentListEntry entry = getListEntryByEvent(e);
			if (entry != null)
				entry.mouseEventHandled = false;
			forwardEvent(e);

			if (entry != null && !entry.mouseEventHandled)
				forwardEventToEntry(e, entry);
		}

		private void forwardEventToEntry(MouseEvent e, ComponentListEntry entry) {
			Point point = SwingUtilities.convertPoint(this.glass, e.getPoint(), entry);
			MouseEvent event = new MouseEvent(entry, e.getID(), e.getWhen(), e.getModifiers(),
				point.x, point.y, e.getClickCount(), e.isPopupTrigger());
			delegateMouseEvent(event);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			ComponentListEntry entry = getListEntryByEvent(e);
			this.that.selectAndNotifyListeners(entry);
			if (entry != null)
				entry.mouseEventHandled = false;
			forwardEvent(e);
			if (entry != null && !entry.mouseEventHandled)
				forwardEventToEntry(e, entry);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			ComponentListEntry entry = getListEntryByEvent(e);
			if (entry != null)
				entry.mouseEventHandled = false;
			forwardEvent(e);
			if (entry != null && !entry.mouseEventHandled)
				forwardEventToEntry(e, entry);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			forwardEvent(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			ComponentListEntry hovered = getListEntryByEvent(e);
			if (hovered != this.currentEntryHovered) {
				if (this.currentEntryHovered != null)
					this.currentEntryHovered.setHover(false);
				if (hovered != null)
					hovered.setHover(true);
				this.currentEntryHovered = hovered;
			}

			forwardEvent(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			forwardEvent(e);
		}

		public ComponentListEntry getListEntryByEvent(MouseEvent e) {
			for (TupleR<ComponentListEntry, T> c : this.that.entries) {
				Point destPoint = SwingUtilities.convertPoint(this.glass, e.getPoint(), c.e0);
				if (c.e0.contains(destPoint))
					return c.e0;
			}
			return null;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			ComponentListEntry hovered = getListEntryByEvent(e);
			if (hovered != this.currentEntryHovered) {
				if (this.currentEntryHovered != null)
					this.currentEntryHovered.setHover(false);
				if (hovered != null)
					hovered.setHover(true);
				this.currentEntryHovered = hovered;
			}

			forwardEvent(e);
		}

		private void forwardEvent(MouseEvent e) {
			Point glassPoint = e.getPoint();

			Point containerPoint = SwingUtilities.convertPoint(this.glass, glassPoint, this.con);
			if (containerPoint.y < 0)
				return;

			Component component = SwingUtilities.getDeepestComponentAt(this.con,
				containerPoint.x, containerPoint.y);
			if (component != this.currentlyHovered && this.currentlyHovered != null) {
				Point hoverPoint = SwingUtilities.convertPoint(this.glass, glassPoint,
					this.currentlyHovered);
				this.currentlyHovered.dispatchEvent(new MouseEvent(this.currentlyHovered,
					MouseEvent.MOUSE_EXITED, e.getWhen(), e.getModifiers(), hoverPoint.x,
					hoverPoint.y, e.getClickCount(), e.isPopupTrigger()));
			}
			if (component == null)
				return;

			Point componentPoint = SwingUtilities.convertPoint(this.glass, glassPoint, component);

			if (component != this.currentlyHovered) {
				this.currentlyHovered = component;
				this.currentlyHovered.dispatchEvent(new MouseEvent(component,
					MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiers(), componentPoint.x,
					componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
			}

			component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e
				.getModifiers(), componentPoint.x, componentPoint.y, e.getClickCount(), e
				.isPopupTrigger()));
		}

	}

	protected void delegateMouseEvent(MouseEvent e) {
		if (e.getID() == MouseEvent.MOUSE_CLICKED)
			for (MouseListener l : this.mouseListeners)
				l.mouseClicked(e);
		else if (e.getID() == MouseEvent.MOUSE_PRESSED)
			for (MouseListener l : this.mouseListeners)
				l.mousePressed(e);
		else if (e.getID() == MouseEvent.MOUSE_RELEASED)
			for (MouseListener l : this.mouseListeners)
				l.mouseReleased(e);
	}

}
