package ch.judos.generic.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Changed by Julian Schelker, 2010 <br>
 * added the possibility to aligne the component<br>
 * MySwing: Advanced Swing Utilites 3 * Copyright (C) 2005 Santhosh Kumar T This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * @since ??.??.2010
 * @author Santhosh Kumar T, Julian Schelker
 * @version 1.01 / 27.02.2013
 */

public class ComponentTitledBorder implements Border, MouseListener, MouseMotionListener,
	SwingConstants {

	/**
	 * an example to show you what this class does
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final JPanel proxyPanel = new JPanel();
		proxyPanel.add(new JLabel("Proxy Host: "));
		proxyPanel.add(new JTextField("proxy.xyz.com"));
		proxyPanel.add(new JLabel("  Proxy Port"));
		proxyPanel.add(new JTextField("8080"));
		final JCheckBox checkBox = new JCheckBox("Use Proxy", true);
		checkBox.setFocusPainted(false);
		ComponentTitledBorder componentBorder = new ComponentTitledBorder(checkBox,
			proxyPanel, BorderFactory.createEtchedBorder());
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean enable = checkBox.isSelected();
				Component comp[] = proxyPanel.getComponents();
				for (int i = 0; i < comp.length; i++) {
					comp[i].setEnabled(enable);
				}
			}
		});
		proxyPanel.setBorder(componentBorder);
		JFrame frame = new JFrame("ComponentTitledBorder - santhosh@in.fiorano.com");
		Container contents = frame.getContentPane();
		contents.setLayout(new FlowLayout());
		contents.add(proxyPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	int offset = 5;

	alignement componentAlignement;

	private int left;

	private Component comp;
	private JComponent container;
	private Rectangle rect;
	private Border border;
	private boolean mouseEntered = false;

	/**
	 * standard alignement (left)
	 * 
	 * @param comp
	 * @param container
	 * @param border
	 */
	public ComponentTitledBorder(Component comp, JComponent container, Border border) {
		this(comp, container, border, alignement.LEFT);
	}

	/**
	 * @param comp
	 * @param container
	 * @param border
	 * @param compAlignement
	 */
	public ComponentTitledBorder(Component comp, JComponent container, Border border,
		alignement compAlignement) {
		this.comp = comp;
		this.componentAlignement = compAlignement;
		this.container = container;
		this.border = border;
		this.left = this.offset;
		container.addMouseMotionListener(this);
		container.addMouseListener(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	@Override
	public Insets getBorderInsets(Component c) {
		Dimension size = this.comp.getPreferredSize();
		Insets insets = this.border.getBorderInsets(c);
		insets.top = Math.max(insets.top, size.height);
		return insets;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.border.Border#isBorderOpaque()
	 */
	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		dispatchEvent(me);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// unused
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// unused
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent me) {
		if (this.mouseEntered) {
			this.mouseEntered = false;
			dispatchEvent(me, MouseEvent.MOUSE_EXITED);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent me) {
		if (this.rect == null) {
			return;
		}

		if (this.mouseEntered == false && this.rect.contains(me.getX(), me.getY())) {
			this.mouseEntered = true;
			dispatchEvent(me, MouseEvent.MOUSE_ENTERED);
		}
		else if (this.mouseEntered == true) {
			if (this.rect.contains(me.getX(), me.getY()) == false) {
				this.mouseEntered = false;
				dispatchEvent(me, MouseEvent.MOUSE_EXITED);
			}
			else {
				dispatchEvent(me, MouseEvent.MOUSE_MOVED);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		dispatchEvent(me);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		dispatchEvent(me);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.border.Border#paintBorder(java.awt.Component,
	 *      java.awt.Graphics, int, int, int, int)
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Insets borderInsets = this.border.getBorderInsets(c);
		Insets insets = getBorderInsets(c);
		int temp = (insets.top - borderInsets.top) / 2;
		this.border.paintBorder(c, g, x, y + temp, width, height - temp);
		Dimension size = this.comp.getPreferredSize();
		switch (this.componentAlignement) {
			case LEFT :
				this.left = this.offset;
				break;
			case CENTER :
				this.left = width / 2 - size.width / 2;
				break;
			case RIGHT :
				this.left = width - size.width - this.offset;
				break;
			default :
				System.err.println("ComponentAlignement enum not known: "
					+ this.componentAlignement);
				break;
		}
		this.rect = new Rectangle(this.left, 0, size.width, size.height);

		SwingUtilities.paintComponent(g, this.comp, (Container) c, this.rect);
	}

	private void dispatchEvent(MouseEvent me) {
		if (this.rect != null && this.rect.contains(me.getX(), me.getY())) {
			dispatchEvent(me, me.getID());
		}
		else
			dispatchEvent(me, MouseEvent.MOUSE_EXITED);
	}

	private void dispatchEvent(MouseEvent me, int id) {
		Point pt = me.getPoint();
		pt.translate(-this.rect.x, 0);

		this.comp.setSize(this.rect.width, this.rect.height);
		this.comp.dispatchEvent(new MouseEvent(this.comp, id, me.getWhen(), me.getModifiers(),
			pt.x, pt.y, me.getClickCount(), me.isPopupTrigger(), me.getButton()));
		if (!this.comp.isValid()) {
			this.container.repaint();
		}
	}

	@SuppressWarnings("all")
	enum alignement {
		LEFT, CENTER, RIGHT
	}

}
