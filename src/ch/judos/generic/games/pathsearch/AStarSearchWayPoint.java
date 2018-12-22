package ch.judos.generic.games.pathsearch;

/**
 * defines a waypoint with a known target<br>
 * is able to compare the heuristic for waypoints
 * 
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.02 / 25.11.2011
 */
public class AStarSearchWayPoint extends SimpleWayPoint implements
	Comparable<AStarSearchWayPoint> {

	private static final long serialVersionUID = -6338254904838453889L;
	/**
	 * position x
	 */
	protected int ty;
	/**
	 * position y
	 */
	protected int tx;
	/**
	 * steps taken already
	 */
	protected int steps;
	/**
	 * remaining estimated distance
	 */
	protected double dist;

	/**
	 * creates a search waypoint
	 * 
	 * @param x
	 *            position of the waypoint
	 * @param y
	 *            position of the waypoint
	 * @param steps
	 *            how many steps have been taken since start
	 * @param tx
	 *            position of target
	 * @param ty
	 *            position of target
	 */
	public AStarSearchWayPoint(int x, int y, int steps, int tx, int ty) {
		super(x, y);
		this.steps = steps;
		this.tx = tx;
		this.ty = ty;
		this.dist = Math.hypot(x - tx, y - ty);
	}

	/**
	 * creates a search waypoint
	 * 
	 * @param start
	 *            location of the waypoint
	 * @param steps
	 *            how many steps have been taken since start
	 * @param tx
	 *            position of target
	 * @param ty
	 *            position of target
	 */
	public AStarSearchWayPoint(WayPoint start, int steps, int tx, int ty) {
		this(start.getX(), start.getY(), steps, tx, ty);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @param o
	 * @return comparision value
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AStarSearchWayPoint o) {
		double a = this.steps + this.dist;
		double b = o.steps + o.dist;
		if (a < b)
			return -1;
		else if (a == b)
			return 0;
		else
			return 1;
	}

	/**
	 * <b>note:</b> check whether the neighbors exist on a limited gridmap
	 * 
	 * @return 4 neighbors assuming the fields on a grid have 4 connectedness
	 *         (left,right,top,bottom)
	 */
	public AStarSearchWayPoint[] neighbors4connectedness() {
		int s = this.steps + 1;
		AStarSearchWayPoint w1, w2, w3, w4;
		w1 = new AStarSearchWayPoint(this.x - 1, this.y, s, this.tx, this.ty);
		w2 = new AStarSearchWayPoint(this.x + 1, this.y, s, this.tx, this.ty);
		w3 = new AStarSearchWayPoint(this.x, this.y - 1, s, this.tx, this.ty);
		w4 = new AStarSearchWayPoint(this.x, this.y + 1, s, this.tx, this.ty);
		return new AStarSearchWayPoint[]{w1, w2, w3, w4};
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "A*WayPoint: " + this.x + "/" + this.y + ", steps:" + this.steps + ", dist:"
			+ this.dist;
	}

}