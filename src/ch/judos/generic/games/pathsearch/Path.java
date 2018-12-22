package ch.judos.generic.games.pathsearch;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.DynamicList.Iterator2;
import ch.judos.generic.data.geometry.PointF;

/**
 * @since 22.02.2013
 * @author Julian Schelker
 * @version 1.01 / 24.02.2013
 */
public class Path implements Cloneable, Comparable<Path> {
	/**
	 * @param p1
	 * @param p2
	 * @return sum of coordinates of points
	 */
	protected static Point addP(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}

	/**
	 * @param start
	 * @param target
	 * @return minX,minY,maxX,maxY of the rectangle speficied by the boundingbox
	 *         of the two waypoints
	 */
	protected static int[] calcHull(WayPoint start, WayPoint target) {
		int[] r = new int[4];
		r[0] = Math.min(start.getX(), target.getX());
		r[1] = Math.min(start.getY(), target.getY());
		r[2] = Math.max(start.getX(), target.getX());
		r[3] = Math.max(start.getY(), target.getY());
		return r;
	}

	/**
	 * @param trans
	 * @param points
	 * @return minX,minY,maxX,maxY of the rectangle speficied by the boundingbox
	 *         of the two waypoints
	 */
	protected static int[] calcHullForPoints(CoordinateTranslator trans, PointF... points) {
		int m = -trans.getGridSize() / 2;
		int p = trans.getGridSize() / 2 - 1;
		Point mP = new Point(m, m);
		Point pP = new Point(p, p);

		int[] r = new int[4];
		for (PointF point : points) {
			Point min = addP(point.getPoint(), mP);
			r[0] = Math.min(r[0], min.x);
			r[1] = Math.min(r[1], min.y);
			Point max = addP(point.getPoint(), pP);
			r[2] = Math.max(r[2], max.x);
			r[3] = Math.max(r[3], max.y);
		}
		WayPoint w1 = trans.getFieldFromPoint(new PointF(r[0], r[1]));
		WayPoint w2 = trans.getFieldFromPoint(new PointF(r[2], r[3]));
		return new int[]{w1.getX(), w1.getY(), w2.getX(), w2.getY()};
	}

	/**
	 * @param start
	 * @param target
	 * @return an index between 0 and 3 <br>
	 *         0 = down right (angle between 0 and 90)<br>
	 *         1 = down left (angle between 90 and 180)<br>
	 *         2 = up left (angle between -180 and -90)<br>
	 *         3 = up right (angle between -90 and 0)<br>
	 */
	protected static int findDirection(PointF start, PointF target) {
		double d = Math.atan2(target.getY() - start.getY(), target.getX() - start.getX());
		return (int) ((Math.floor(d / Math.PI * 2)) + 4) % 4;
	}

	/**
	 * @param start
	 * @param target
	 * @return an index between 0 and 3 <br>
	 *         0 = down right (angle between 0 and 90)<br>
	 *         1 = down left (angle between 90 and 180)<br>
	 *         2 = up left (angle between -180 and -90)<br>
	 *         3 = up right (angle between -90 and 0)<br>
	 */
	protected static int findDirection(WayPoint start, WayPoint target) {
		double d = Math.atan2(target.getY() - start.getY(), target.getX() - start.getX());
		return (int) ((Math.floor(d / Math.PI * 2)) + 4) % 4;
	}

	private List<WayPoint> way;

	/**
	 * creates a path
	 * 
	 * @param way
	 *            all waypoint that are visited by the path
	 */
	public Path(List<WayPoint> way) {
		this.way = way;
	}

	/**
	 * creates a path
	 * 
	 * @param way
	 *            all waypoints that are visited by the path
	 */
	public Path(WayPoint[] way) {
		this.way = Arrays.asList(way);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Path(new ArrayList<>(this.way));
	}

	/**
	 * @return new instance of the same path
	 */
	public Path cloneT() {
		return new Path(new ArrayList<>(this.way));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @param o
	 * @return comparision value
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Path o) {
		return (int) Math.signum(distance() - o.distance());
	}

	/**
	 * @return the length of this path, going diagonally from waypoint to
	 *         waypoint
	 */
	protected float distance() {
		float dist = 0;
		Iterator2 it = DynamicList.getIterator2AtOnce(this.way);
		while (it.hasNext()) {
			WayPoint[] ele = (WayPoint[]) it.next();
			dist += ele[0].distance(ele[1]);
		}
		return dist;
	}

	/**
	 * @param fieldChecker
	 *            object that knows which fields are free
	 * @param transl
	 *            coordinate Translator
	 * @return a shortened way to follow to be faster
	 */
	public Path getAirWay(FreeFieldChecker fieldChecker, CoordinateTranslator transl) {
		if (this.way.size() < 3)
			return cloneT();
		Path path1 = new Path(shortenForward(this.way, fieldChecker, transl));
		Path path2 = shortenBackward(this.way, fieldChecker, transl);
		return DynamicList.smallest(path1, path2);
	}

	/**
	 * @param field
	 * @param fieldSize
	 * @return the point in pixels of the center for the specified field
	 */
	protected Point getPoint(WayPoint field, int fieldSize) {
		int f2 = fieldSize / 2;
		return new Point(field.getX() * fieldSize + f2, field.getY() * fieldSize + f2);
	}

	/**
	 * @return all waypoint visited by the path
	 */
	public List<WayPoint> getPoints() {
		return this.way;
	}

	/**
	 * @param start
	 * @param target
	 * @param freeCheck
	 * @param trans
	 *            coordinateTranslator
	 * @return whether the fields are diagonally reachable
	 */
	public boolean isAirReachable(PointF start, PointF target, FreeFieldChecker freeCheck,
		CoordinateTranslator trans) {
		int i = findDirection(start, target);
		int size = trans.getGridSize();
		int m = -size / 2;
		int p = size / 2 - 1;
		Point[] corners = new Point[]{new Point(p, m), new Point(p, p), new Point(m, p),
			new Point(m, m)};
		Point rc1 = corners[i];
		Point rc2 = corners[(i + 2) % 4];
		Point lineStart = start.getPoint();
		Point lineEnd = target.getPoint();

		Line2D l1 = new Line2D.Float(addP(lineStart, rc1), addP(lineEnd, rc1));
		Line2D l2 = new Line2D.Float(addP(lineStart, rc2), addP(lineEnd, rc2));

		int[] hull = calcHullForPoints(trans, start, target);
		Point cur;
		SimpleWayPoint wp;
		for (int x = hull[0]; x <= hull[2]; x++) {
			for (int y = hull[1]; y <= hull[3]; y++) {
				wp = new SimpleWayPoint(x, y);
				if (!freeCheck.isFree(wp) && !start.equals(wp)) {
					cur = getPoint(wp, size);
					cur.x += m;
					cur.y += m;
					if (l1.intersects(cur.x, cur.y, size, size))
						return false;
					if (l2.intersects(cur.x, cur.y, size, size))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * @return the path traversed backwards
	 */
	public Path reverse() {
		return new Path(reverseList(this.way));
	}

	/**
	 * @param <T>
	 * @param list
	 * @return same list with reversed order of elements
	 */
	public <T> List<T> reverseList(List<T> list) {
		List<T> result = new ArrayList<>();
		for (T t : list) {
			result.add(0, t);
		}
		return result;
	}

	/**
	 * @param foundWay
	 * @param fieldChecker
	 * @param transl
	 * @return the path shortened, diagonals are used first from the target
	 */
	@SuppressWarnings("all")
	protected Path shortenBackward(List<WayPoint> foundWay, FreeFieldChecker fieldChecker,
		CoordinateTranslator transl) {
		foundWay = reverseList(foundWay);
		List<WayPoint> path = shortenForward(foundWay, fieldChecker, transl);
		return new Path(reverseList(path));
	}

	/**
	 * @param path
	 *            the path to shorten
	 * @param fieldChecker
	 * @param transl
	 * @return the shortened path, diagonals are used first from initial
	 *         starting field
	 */
	protected List<WayPoint> shortenForward(List<WayPoint> path,
		FreeFieldChecker fieldChecker, CoordinateTranslator transl) {
		List<WayPoint> result = new ArrayList<>();
		Iterator<WayPoint> it = path.iterator();
		WayPoint from = it.next();
		result.add(from);
		WayPoint last = it.next();
		WayPoint cur = null;
		do {
			cur = it.next();
			// if (!isAirReachable(from, cur, fieldChecker)) {
			PointF pointFrom = transl.getPointFromField(from);
			PointF pointTo = transl.getPointFromField(cur);
			if (!isAirReachable(pointFrom, pointTo, fieldChecker, transl)) {
				result.add(last);
				from = last;
			}
			last = cur;
		} while (it.hasNext());
		result.add(cur);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("D=" + distance() + "  way:");
		for (WayPoint w : this.way)
			b.append(w.getX() + "/" + w.getY() + " ");
		return b.toString();
	}
}
