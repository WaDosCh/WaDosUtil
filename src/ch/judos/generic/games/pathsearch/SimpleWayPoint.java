package ch.judos.generic.games.pathsearch;

import java.io.Serializable;

/**
 * defines a simple location on a map
 * 
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.11 / 24.02.2013
 */
public class SimpleWayPoint extends WayPoint implements Serializable {

	private static final long serialVersionUID = -2378580491710419827L;
	/**
	 * x-coordinate of the waypoint
	 */
	protected int x;
	/**
	 * y-coordinate of the waypoint
	 */
	protected int y;

	/**
	 * @param x
	 * @param y
	 */
	public SimpleWayPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.WayPoint#getX()
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.WayPoint#getY()
	 */
	@Override
	public int getY() {
		return this.y;
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