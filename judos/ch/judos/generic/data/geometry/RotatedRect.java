package ch.judos.generic.data.geometry;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/**
 * @created 24.04.2012
 * @author Julian Schelker
 */
public class RotatedRect {

	private int h;
	private int w;
	private double y;
	private double x;
	private double theta;
	private double r;
	private double alpha;
	private double innerR;

	public RotatedRect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.theta = 0;
		init();
	}

	public void moveAbsolute(double xPos, double yPos) {
		this.x += xPos;
		this.y += yPos;
	}

	public void moveRelative(double xPos, double yPos) {
		this.x += yPos * Math.cos(this.theta) + xPos * Math.sin(this.theta + Math.PI);
		this.y += yPos * Math.sin(this.theta) - xPos * Math.cos(this.theta + Math.PI);
	}

	private void init() {
		this.r = Math.hypot(this.w / 2, this.h / 2);
		this.alpha = Math.atan2(this.h / 2, this.w / 2);
		this.innerR = Math.min(this.w / 2, this.h / 2);
	}

	private double[] getPointsAngles() {
		double pi = Math.PI;
		return new double[]{this.theta + this.alpha, this.theta - this.alpha,
			this.theta + pi + this.alpha, this.theta + pi - this.alpha};
	}

	private int[] getXPoints() {
		int[] xPos = new int[4];
		double[] a = getPointsAngles();
		for (int i = 0; i < 4; i++)
			xPos[i] = (int) (this.x + this.r * Math.cos(a[i]));
		return xPos;
	}

	private int[] getYPoints() {
		int[] yPos = new int[4];
		double[] a = getPointsAngles();
		for (int i = 0; i < 4; i++)
			yPos[i] = (int) (this.y + this.r * Math.sin(a[i]));
		return yPos;
	}

	private Point2D getPoint(double angle) {
		return new Point2D.Double(this.x + this.r * Math.cos(angle), this.y + this.r
			* Math.sin(angle));
	}

	private Polygon getPoly() {
		return new Polygon(getXPoints(), getYPoints(), 4);
	}

	/**
	 * takes between 500ns and 2qs
	 * 
	 * @param rect
	 * @return whether this intersects with r
	 */
	public boolean intersects(RotatedRect rect) {
		double d = distanceTo(rect);
		if (d >= this.r + rect.r)
			return false;
		if (d <= this.innerR + rect.innerR)
			return true;
		if (this.containsPointsOf(rect))
			return true;
		if (rect.containsPointsOf(this))
			return true;
		return false;
	}

	public boolean containsPointsOf(RotatedRect rect) {
		Polygon p = this.getPoly();
		double[] angles = rect.getPointsAngles();
		for (int i = 0; i < 4; i++) {
			Point2D vertex = rect.getPoint(angles[i]);
			if (p.contains(vertex))
				return true;
		}
		return false;
	}

	public double distanceTo(RotatedRect rect) {
		return Math.hypot(rect.x - this.x, rect.y - this.y);
	}

	public void draw(Graphics2D g) {
		g.drawPolygon(getPoly());
	}

	public void fill(Graphics2D g) {
		g.fillPolygon(getPoly());
	}

	public void rotate(double d) {
		this.theta += d;
	}

	public void setPosition(Point location) {
		if (location == null)
			return;
		this.x = location.x;
		this.y = location.y;
	}

}
