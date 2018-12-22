package ch.judos.generic.games.pathsearch;

import java.util.HashSet;

import ch.judos.generic.data.geometry.PointF;

/**
 * defines an abstract grid map, the size of the map is always known and the
 * grid consists of rectangular located fields<br>
 * fields are assumed to be squares
 * 
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.01 / 24.02.2013
 */
public abstract class GridMap implements CoordinateTranslator {
	/**
	 * @param p
	 *            point in pixels on the map
	 * @return a field where this pixels belongs to
	 */
	@Override
	public WayPoint getFieldFromPoint(PointF p) {
		return new SimpleWayPoint((int) (p.getX() / getGridSize()),
			(int) (p.getY() / getGridSize()));
	}

	/**
	 * @return the width and height of a field
	 */
	@Override
	public abstract int getGridSize();

	/**
	 * @return the height of the map in fields
	 */
	public abstract int getHeight();

	/**
	 * @param point
	 *            some field on the map
	 * @return the coordinates of the center of this field
	 */
	@Override
	public PointF getPointFromField(WayPoint point) {
		int g = getGridSize();
		int x = point.getX() * g + g / 2;
		int y = point.getY() * g + g / 2;
		return new PointF(x, y);
	}

	/**
	 * @return the width of the map in fields
	 */
	public abstract int getWidth();

	/**
	 * @param fields
	 * @return whether all specified fields are free
	 */
	public boolean isFree(HashSet<WayPoint> fields) {
		for (WayPoint w : fields)
			if (!isFree(w))
				return false;
		return true;
	}

	/**
	 * @param field
	 *            a field on the map
	 * @return whether this waypoint is not occupied by another object
	 */
	public abstract boolean isFree(WayPoint field);

	/**
	 * @param field
	 *            a field on the map
	 * @return whether this waypoint is inside the map
	 */
	public abstract boolean isInsideMap(WayPoint field);
}