package ch.judos.generic.gui;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * @since 20.09.2014
 * @author Julian Schelker
 */
public class ComponentListEntry extends JPanel {

	public static final Color selected = Color.LIGHT_GRAY;
	public static final Color hovered = new Color(210, 210, 210);
	public static final Color defaultColor = new Color(238, 238, 238);

	private static final long serialVersionUID = 6296764881311212990L;

	protected boolean isSelected = false;
	protected boolean isHovered = false;
	public boolean mouseEventHandled;

	public ComponentListEntry() {
		this.setBackground(getBGColorDefault());
	}

	public Color getBGColorSelected() {
		return selected;
	}

	public Color getBGColorHovered() {
		return hovered;
	}

	public Color getBGColorDefault() {
		return defaultColor;
	}

	/**
	 * this is called whenever isSelected or isHovered fields change. use it to
	 * change the visuals of the entry
	 */
	protected void updateHoverSelected() {
		Color c = getBGColorDefault();
		if (this.isHovered)
			c = getBGColorHovered();
		if (this.isSelected)
			c = getBGColorSelected();
		this.setBackground(c);
	}

	protected void setHoverSelected(boolean hover, boolean selected) {
		this.isHovered = hover;
		this.isSelected = selected;
		updateHoverSelected();
	}

	protected void setHover(boolean hover) {
		this.isHovered = hover;
		updateHoverSelected();
	}

	protected void setSelected(boolean selected) {
		this.isSelected = selected;
		updateHoverSelected();
	}

}
