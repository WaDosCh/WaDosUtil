package ch.judos.generic.games.navigation.model;

import java.awt.Color;
import java.awt.Graphics;

import ch.judos.generic.games.pathsearch.SimpleWayPoint;
import ch.judos.generic.graphics.Drawable;

/**
 * 
 */

/**
 * @author Julian Schelker
 * @version 1.0
 * @dependsOn
 */
public class Rock implements Drawable {

	private int x;
	private int y;

	/**
	 * @param map
	 * @param gridx
	 * @param gridy
	 */
	public Rock(Map map, int gridx, int gridy) {
		this.x = gridx;
		this.y = gridy;
		map.reserveField(new SimpleWayPoint(this.x, this.y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Drawable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillOval(this.x * 50, this.y * 50, 50, 50);
	}

}
