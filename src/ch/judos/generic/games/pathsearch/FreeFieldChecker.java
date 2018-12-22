package ch.judos.generic.games.pathsearch;

import java.util.HashSet;

/**
 * @since 20.02.2013
 * @author Julian Schelker
 * @version 1.01 / 24.02.2013
 */
public interface FreeFieldChecker {

	/**
	 * @param fields
	 * @return whether the specified waypoints are all free<br>
	 *         Note: this is less secure than "isFreeAndInsideMap", if you are
	 *         not totally sure that waypoint is inside the map, use
	 *         "isFreeAndInsideMap"!
	 */
	public boolean isFree(HashSet<WayPoint> fields);

	/**
	 * @param p
	 * @return whether the specified waypoint is free<br>
	 *         Note: this is less secure than "isFreeAndInsideMap", if you are
	 *         not totally sure that waypoint is inside the map, use
	 *         "isFreeAndInsideMap"!
	 */
	public boolean isFree(WayPoint p);

	/**
	 * @param p
	 * @return whether the specified waypoint is free and inside the map
	 */
	public boolean isFreeAndInsideMap(WayPoint p);
}
