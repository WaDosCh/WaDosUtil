package ch.judos.generic.games.pathsearch;

import ch.judos.generic.data.geometry.PointF;

/**
 * @since 04.08.2013
 * @author Julian Schelker
 */
public interface CoordinateTranslator {
	/**
	 * @param p
	 *            point in pixels on the map
	 * @return a field where this pixels belongs to
	 */
	public WayPoint getFieldFromPoint(PointF p);

	/**
	 * @return size of the grid
	 */
	public int getGridSize();

	/**
	 * @param point
	 *            some field on the map
	 * @return the coordinates of the center of this field
	 */
	public PointF getPointFromField(WayPoint point);
}
