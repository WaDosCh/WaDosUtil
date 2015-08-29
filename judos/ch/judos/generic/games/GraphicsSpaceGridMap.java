package ch.judos.generic.games;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import ch.judos.generic.games.pathsearch.SimpleWayPoint;
import ch.judos.generic.games.pathsearch.WayPoint;
import ch.judos.generic.games.unitCoordination.SpaceGridMap;
import ch.judos.generic.graphics.Drawable;

/**
 * extends the behaviour of a SpaceGridMap by features like drawing tilesets
 * onto the map
 * 
 * @since 02.03.2013
 * @author Julian Schelker
 * @version 1.0 / 02.03.2013
 */
public abstract class GraphicsSpaceGridMap extends SpaceGridMap implements Drawable {

	/**
	 * the scroll object
	 */
	protected Scroll scroll;
	private int drawFieldsY;
	private int drawFieldsX;

	/**
	 * @param mapSize
	 *            the map size
	 * @param scroll
	 *            object responsible for scrolling
	 * @param viewPort
	 *            size of the visible area on the screen
	 */
	public GraphicsSpaceGridMap(Dimension mapSize, Dimension viewPort, Scroll scroll) {
		super(mapSize.width, mapSize.height);
		this.scroll = scroll;
		this.drawFieldsX = viewPort.width / getGridSize() + 2;
		this.drawFieldsY = viewPort.height / getGridSize() + 2;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.graphics.Drawable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Point p = this.scroll.getPosition();
		int fieldStartX = p.x / getGridSize();
		int fieldStartY = p.y / getGridSize();
		for (int fx = 0; fx < this.drawFieldsX; fx++)
			for (int fy = 0; fy < this.drawFieldsY; fy++) {
				int fxR = fx + fieldStartX;
				int fyR = fy + fieldStartY;
				WayPoint w = new SimpleWayPoint(fxR, fyR);
				if (isInsideMap(w)) {
					int drawX = fxR * getGridSize() - p.x;
					int drawY = fyR * getGridSize() - p.y;
					Point draw = new Point(drawX, drawY);
					paintField(w, draw, g);
				}
			}
	}

	/**
	 * requests the map to draw the specified field
	 * 
	 * @param w
	 *            the position of the field to draw
	 * @param draw
	 *            the coordinates where to draw
	 * @param g
	 *            the context to draw everything on
	 */
	protected abstract void paintField(WayPoint w, Point draw, Graphics g);

}
