package ch.judos.generic.games.pathsearch;

import java.util.Arrays;

/**
 * defines an immutable 2D Point on an grid
 * 
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.01 / 17.02.2013
 */
public abstract class WayPoint {

	/**
	 * @param wayPoint
	 * @return the air distance between this and the given point
	 */
	public float distance(WayPoint wayPoint) {
		return (float) Math.hypot(getX() - wayPoint.getX(), getY() - wayPoint.getY());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof WayPoint))
			return false;
		WayPoint that = (WayPoint) obj;
		return this.equals(that);
	}

	/**
	 * @param p
	 *            another waypoint
	 * @return checks if they represent the same location on the map
	 */
	public boolean equals(WayPoint p) {
		return getX() == p.getX() && getY() == p.getY();
	}

	/**
	 * @return the x position of the place
	 */
	public abstract int getX();

	/**
	 * @return the y position of the place
	 */
	public abstract int getY();

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(new int[]{getX(), this.getY()});
	}

}