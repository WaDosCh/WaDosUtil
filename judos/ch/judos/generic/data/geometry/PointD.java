package ch.judos.generic.data.geometry;

import java.awt.geom.Point2D;

/**
 * @since 09.02.2015
 * @author Julian Schelker
 */
public class PointD extends Point2D.Double {

	private static final long serialVersionUID = -3551698231506636754L;

	public void rotate(double angleRadian) {
		double rx = (this.x * Math.cos(angleRadian)) - (this.y * Math.sin(angleRadian));
		double ry = (this.x * Math.sin(angleRadian)) + (this.y * Math.cos(angleRadian));
		this.x = rx;
		this.y = ry;
	}

	public void rotate(Angle angle) {
		rotate(angle.getRadian());
	}
}
