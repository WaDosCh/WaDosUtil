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

	/**
	 * used for the RStorage to create instances
	 */
	@SuppressWarnings("unused")
	private PointD() {
	}

	/**
	 * @param x
	 * @param y
	 */
	public PointD(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public PointD(Point2D p) {
		this(p.getX(), p.getY());
	}

	/**
	 * <b>Note:</b> does not change the current instance
	 * 
	 * @param p
	 *            some other point
	 * @return the new point, adding up the position of this point and the given
	 *         one
	 */
	public PointD add(Point2D p) {
		return new PointD(this.x + p.getX(), this.y + p.getY());
	}

	/**
	 * <b>Note:</b> changes the current instance
	 * 
	 * @param p
	 *            some other point
	 */
	public void addI(Point2D p) {
		this.x += p.getX();
		this.y += p.getY();
	}

	/**
	 * @param target
	 * @param speed
	 *            distance to move the point in the direction of the target
	 * @return whether target is reached or not
	 */
	public boolean approachPoint(Point2D target, double speed) {
		PointD targetD = new PointD(target);
		PointD delta = targetD.subtract(this);
		double dist = delta.hypot();
		if (dist <= speed) {
			this.x = targetD.x;
			this.y = targetD.y;
			return true;
		}
		double angle = Math.atan2(delta.y, delta.x);
		movePointI(angle, speed);
		return false;
	}

	/**
	 * get a copy of this point<br>
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public PointD clone() {
		return new PointD(this.x, this.y);
	}

	/**
	 * @param pos
	 * @return the distance between this and the given point
	 */
	public double distanceTo(Point2D pos) {
		return Math.hypot(this.x - pos.getX(), this.y - pos.getY());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PointD))
			return false;
		PointD p = (PointD) obj;
		return p.x == this.x && p.y == this.y;
	}

	public double getAngleTo(Point2D target) {
		return Math.atan2(target.getY() - this.y, target.getX() - this.x);
	}

	public Angle getAAngleTo(Point2D target) {
		return Angle.fromRadian(Math.atan2(target.getY() - this.y, target.getX() - this.x));
	}

	/**
	 * @return a common awt int-based point
	 */
	public PointI getPoint() {
		return new PointI(getXI(), getYI());
	}

	public PointI i() {
		return getPoint();
	}

	/**
	 * @return X as integer
	 */
	public int getXI() {
		return (int) Math.round(this.x);
	}

	/**
	 * @return Y as integer
	 */
	public int getYI() {
		return (int) Math.round(this.y);
	}

	/**
	 * @return calculates the hypthenus of this point itself, representing the
	 *         distance to the coordinates' origin
	 */
	public double hypot() {
		return Math.hypot(this.x, this.y);
	}

	public void movePointI(double angle, double step) {
		this.x += (float) (step * Math.cos(angle));
		this.y += (float) (step * Math.sin(angle));
	}

	public void movePointI(Angle angle, double step) {
		this.x += (float) (step * angle.getCos());
		this.y += (float) (step * angle.getSin());
	}

	public PointD movePoint(double angle, double step) {
		return new PointD(this.x + (float) (step * Math.cos(angle)), this.y + (float) (step
			* Math.sin(angle)));
	}

	public PointD movePoint(Angle angle, double step) {
		return this.add(angle.getDirection().scale(step));
	}

	/**
	 * @param factor
	 * @return the length of the vector is multiplied by the given factor and
	 *         the new vector is returned
	 */
	public PointD scale(double factor) {
		return new PointD(this.x * factor, this.y * factor);
	}

	public void scaleI(double factor) {
		this.x *= factor;
		this.y *= factor;
	}

	/**
	 * @param p
	 * @return the new point, where the position is this' point position minus
	 *         the given one
	 */
	public PointD subtract(Point2D p) {
		return new PointD(this.x - p.getX(), this.y - p.getY());
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

	/**
	 * @return the length of the vector represented by this point
	 */
	public double length() {
		return hypot();
	}

	/**
	 * modifies the vector represented by this point such that it has the length
	 * of 1.
	 */
	public void normalize() {
		this.scaleI(1 / this.length());
	}

	/**
	 * sets the value of this point
	 * 
	 * @param x
	 * @param y
	 */
	public void setValue(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * sets the length of the vector represented by this point
	 * 
	 * @param forceMaxLength
	 */
	public void scaleToI(double forceMaxLength) {
		this.scaleI(forceMaxLength / this.length());
	}

	public PointI getPointRounded() {
		return new PointI((int) Math.round(this.x), (int) Math.round(this.y));
	}

}
