package ch.judos.generic.data.geometry;

import java.awt.Point;
import java.awt.geom.Point2D;

import ch.judos.generic.data.rstorage.interfaces.RStorable2;

/**
 * @since 09.02.2015
 * @author Julian Schelker
 */
public class PointI extends Point implements RStorable2 {
	private static final long serialVersionUID = -2304751012108303425L;

	public PointI(Point point) {
		this(point.x, point.y);
	}

	public PointI(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public PointI() {
	}

	public double getAngleTo(Point2D target) {
		return Math.atan2(target.getY() - this.y, target.getX() - this.x);
	}

	public Angle getAAngleTo(Point2D target) {
		return Angle.fromRadian(Math.atan2(target.getY() - this.y, target.getX() - this.x));
	}

	public PointI deepCopy() {
		return new PointI(this.x, this.y);
	}

	public boolean inRectFromZero(int width, int height) {
		return this.x >= 0 && this.x < width && this.y >= 0 && this.y < height;
	}

	public PointF f() {
		return new PointF(this);
	}

	public PointI add(PointF scale) {
		return new PointI(this.x + scale.getXI(), this.y + scale.getYI());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.x + "/" + this.y;
	}

}
