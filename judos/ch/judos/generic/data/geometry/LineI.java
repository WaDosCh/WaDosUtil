package ch.judos.generic.data.geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @since 09.02.2015
 * @author Julian Schelker
 */
public class LineI extends Line2D {

	protected PointI start;
	protected PointI end;

	public LineI() {
	}

	public LineI(int x1, int y1, int x2, int y2) {
		this.start = new PointI(x1, y1);
		this.end = new PointI(x2, y2);
	}

	public LineI(PointI start, PointI end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * creates a line, that starts at the given point and extends length units
	 * into the given angle
	 * 
	 * @param dirPoint
	 * @param length
	 */
	public LineI(DirectedPoint dirPoint, int length) {
		this.start = dirPoint.getPoint();
		this.end = this.start.add(dirPoint.getAAngle().getDirection().scale(length));
	}

	@Override
	public Rectangle2D getBounds2D() {
		int minX = Math.min(this.start.x, this.end.x);
		int width = Math.max(this.start.x - minX, this.end.x - minX);
		int minY = Math.min(this.start.y, this.end.y);
		int height = Math.max(this.start.y - minY, this.end.y - minY);
		return new Rectangle2D.Float(minX, minY, width, height);
	}

	@Override
	public double getX1() {
		return this.start.x;
	}

	@Override
	public double getY1() {
		return this.start.y;
	}

	@Override
	public PointI getP1() {
		return this.start;
	}

	@Override
	public double getX2() {
		return this.end.x;
	}

	@Override
	public double getY2() {
		return this.end.y;
	}

	@Override
	public PointI getP2() {
		return this.end;
	}

	@Override
	public void setLine(double x1, double y1, double x2, double y2) {
		this.start = new PointI((int) x1, (int) y1);
		this.end = new PointI((int) x2, (int) y2);
	}

	/**
	 * 
	 * @param point
	 * @return the distance of the point to the closest point lying on the
	 *         infinitely extended line.<br>
	 *         > 0 if the point lies on the left side, seen from the start point<br>
	 *         < 0 if the point lies on the right side
	 * 
	 */
	public double ptLineDistSigned(Point2D point) {
		return this.start.distance(point)
			* Math.sin(this.start.getAngleTo(this.end) - this.start.getAngleTo(point));
	}

	/**
	 * @see LineI#ptLineDistAlongUnified(Point2D)
	 * @see LineI#ptLineDistAlongOutside(Point2D)
	 * @param point
	 * @return the horizontal distance along this line, perpendicular to the
	 *         distance of the point to the closest point on the infinitely
	 *         extended line
	 */
	public double ptLineDistAlong(Point2D point) {
		double angleRadian = this.start.getAngleTo(this.end);
		double angleRadianB = this.start.getAngleTo(point);
		double dist = this.start.distance(point);
		return dist * Math.cos(angleRadianB - angleRadian);
	}

	/**
	 * @see LineI#ptLineDistAlong(Point2D)
	 * @see LineI#ptLineDistAlongOutside(Point2D)
	 * @param point
	 * @return the horizontal distance along this line, perpendicular to the
	 *         distance of the point to the closest point on the infinitely
	 *         extended line<br>
	 * 
	 *         if the closest location on the line to the given point lies
	 *         within the line, then this returns a value in [0, 1] <br>
	 *         if the point lies horizontally outside and is closer to start,
	 *         this will return a value &lt; 0<br>
	 *         if the point lies outside and is closer to the end, this will
	 *         return a value &gt; 1
	 * 
	 */
	public double ptLineDistAlongUnified(Point2D point) {
		double dist = ptLineDistAlong(point);
		double totalLineDist = this.start.distance(this.end);
		return dist / totalLineDist;
	}

	/**
	 * @see LineI#ptLineDistAlong(Point2D)
	 * @see LineI#ptLineDistAlongUnified(Point2D)
	 * @param point
	 * @return
	 */
	public double ptLineDistAlongOutside(Point2D point) {
		double dist = ptLineDistAlong(point);
		if (dist < 0)
			return dist;
		double totalLineDist = this.start.distance(this.end);
		double delta = dist - totalLineDist;
		if (delta > 0)
			return delta;
		return 0;
	}

}
