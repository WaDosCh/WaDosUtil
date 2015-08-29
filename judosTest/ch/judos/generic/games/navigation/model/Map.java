package ch.judos.generic.games.navigation.model;

import java.awt.Color;
import java.awt.Graphics;

import ch.judos.generic.games.pathsearch.SimpleWayPoint;
import ch.judos.generic.games.unitCoordination.SpaceGridMap;
import ch.judos.generic.graphics.Drawable;

/**
 * @created 10.10.2011
 * @author Julian Schelker
 * @version 1.0
 * @dependsOn
 */
public class Map extends SpaceGridMap {

	Drawable[][] decals;
	Drawable[][] floor;

	public Map(int width, int height) {
		super(width, height);
		this.floor = new Drawable[width][height];
		this.decals = new Drawable[width][height];

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height - 3; y++)
				this.floor[x][y] = new Gras(x, y);
		for (int x = 0; x < width; x++)
			for (int y = height - 3; y < height; y++)
				this.floor[x][y] = new Water(x, y);

		for (int x = 6; x <= 12; x++)
			this.decals[x][5] = new Rock(this, x, 5);
	}

	/**
	 * @param g
	 */
	private void drawGrid(Graphics g) {
		g.setColor(Color.black);
		// draw vertical lines
		for (int x = 0; x < this.gridWidth + 1; x++)
			g.drawLine(x * 50, 0, x * 50, this.gridHeight * 50);
		// draw horizontal lines
		for (int y = 0; y < this.gridHeight + 1; y++)
			g.drawLine(0, y * 50, this.gridWidth * 50, y * 50);
	}

	/*
	 * @see games.pathsearch.GridMap#getGridSize()
	 */
	@Override
	public int getGridSize() {
		return 50;
	}

	public void paint(Graphics g) {

		for (int x = 0; x < this.gridWidth; x++) {
			for (int y = 0; y < this.gridHeight; y++) {
				// draw static object on floor
				if (this.floor[x][y] != null)
					this.floor[x][y].paint(g);
				// draw red bg if field is occupied
				if (!this.fieldFree[x][y]) {
					g.setColor(new Color(255, 200, 200));
					g.fillRect(x * 50, y * 50, 50, 50);
				}
				if (this.decals[x][y] != null)
					this.decals[x][y].paint(g);
			}
		}
		drawGrid(g);
	}

	public static class Gras extends SimpleWayPoint implements Drawable {
		private static final long serialVersionUID = -5926918915881586681L;

		public Gras(int x, int y) {
			super(x, y);
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(new Color(50, 180, 80));
			g.fillRect(this.x * 50, this.y * 50, 50, 50);
		}
	}

	public static class Water extends SimpleWayPoint implements Drawable {
		private static final long serialVersionUID = -5926918915881586681L;

		public Water(int x, int y) {
			super(x, y);
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.blue);
			g.fillRect(this.x * 50, this.y * 50, 50, 50);
		}
	}

}
