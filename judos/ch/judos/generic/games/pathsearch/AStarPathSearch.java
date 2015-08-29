package ch.judos.generic.games.pathsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import ch.judos.generic.data.HashMap2;

/**
 * helps searching paths on maps<br>
 * defines two interfaces for the use of the searchWay function<br>
 * your map and grids on the map should implement those interfaces in order to
 * be able to make use of searchWay(start,target,map)
 * 
 * @since 10.10.2011
 * @author Julian Schelker
 * @version 1.15 / 24.02.2013
 */
public class AStarPathSearch {

	/**
	 * @param start
	 * @param target
	 * @param fieldChecker
	 *            checking for occupied fields
	 * @return a list consisting of the waypoints with the shortest path from
	 *         start to end
	 */
	public Path searchWay(WayPoint start, WayPoint target, FreeFieldChecker fieldChecker) {
		// FreeFieldChecker fieldChecker = new UnitOnMapFreeFieldChecker(map,
		// this.unit);

		if (target == null)
			throw new NullPointerException("target can not be null");

		boolean debugWaySearch = false;

		HashMap2<Integer, Integer, WayPoint> ways = new HashMap2<>();

		ways.put(start.getX(), start.getY(), start);
		PriorityQueue<AStarSearchWayPoint> searchOn = new PriorityQueue<>();
		searchOn.add(new AStarSearchWayPoint(start, 0, target.getX(), target.getY()));
		if (debugWaySearch) {
			System.out.println();
			System.out.println("Start: " + start);
			System.out.println("target: " + target);
		}

		// Search as long as there are possible waypoints to search along
		while (!searchOn.isEmpty()) {
			// Try the next nearest field to the target
			AStarSearchWayPoint cur = searchOn.poll();
			if (debugWaySearch)
				System.out.println("search on with " + cur);
			// Get all neighbors
			AStarSearchWayPoint[] neu = cur.neighbors4connectedness();
			if (debugWaySearch)
				System.out.println("new field candidates: " + neu.length);
			// validate that they are on the map
			for (AStarSearchWayPoint w : neu) {
				if (debugWaySearch)
					System.out.println("trying: " + w);
				// If this way hasn't been found yet
				if (fieldChecker.isFreeAndInsideMap(w) && !ways.containsKey(w.x, w.y)) {
					// If ok, save for further search
					if (debugWaySearch)
						System.out.println("new Field found: " + w);
					searchOn.add(w);
					// save for backtracking
					ways.put(w.x, w.y, cur);
					// case target reached, clear searchOn and leave
					if (target.equals(w)) {
						if (debugWaySearch)
							System.out.println("target found!");
						searchOn.clear();
						break;
					}
				}
				else if (debugWaySearch)
					System.out.println("not free or already checked. :"
						+ fieldChecker.isFreeAndInsideMap(w));
			}
		}
		// No way to target found
		if (!ways.containsKey(target.getX(), target.getY()))
			return null;
		// look up the found way
		List<WayPoint> result = new ArrayList<>();
		WayPoint cur = target;
		if (debugWaySearch)
			System.out.println("way backwards:");
		do {
			// add next plate at beginning
			if (debugWaySearch)
				System.out.println(cur);
			result.add(0, cur);
			cur = ways.get(cur.getX(), cur.getY());
		} while (!start.equals(cur));
		result.add(0, cur);
		return new Path(result);
	}

}
