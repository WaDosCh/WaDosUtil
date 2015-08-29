package ch.judos.generic.data.geometry;

import ch.judos.generic.data.rstorage.interfaces.RStorable2;

/**
 * a point in 2d[int] space that hints into a direction
 * 
 * @since 30.01.2015
 * @author Julian Schelker
 */
public class DirectedPoint implements RStorable2 {
	protected PointI pos;
	protected Angle angle;

	public DirectedPoint(int x, int y, Angle angle) {
		this.pos = new PointI(x, y);
		this.angle = angle;
	}

	public DirectedPoint(PointI point, Angle angle) {
		this.pos = point;
		this.angle = angle;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return this.pos.x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return this.pos.y;
	}

	/**
	 * @return the angle in RADIAN
	 */
	@Deprecated
	public double getAngle() {
		return this.angle.getRadian();
	}

	public Angle getAAngle() {
		return this.angle;
	}

	public PointI getPoint() {
		return this.pos;
	}

	public PointF getPointF() {
		return new PointF(this.pos);
	}

	@Override
	public String toString() {
		return this.pos + " " + this.angle;
	}

	public DirectedPoint move(int step) {
		PointI target = this.pos.f().movePoint(this.angle, step).i();
		return new DirectedPoint(target, this.angle);
	}

	public DirectedPoint turnClockwise(Angle addAngle) {
		return new DirectedPoint(this.pos, this.angle.turnClockwise(addAngle));
	}

	public DirectedPoint turnCounterClockwise(Angle addAngle) {
		return new DirectedPoint(this.pos, this.angle.turnCounterClockwise(addAngle));
	}
}
