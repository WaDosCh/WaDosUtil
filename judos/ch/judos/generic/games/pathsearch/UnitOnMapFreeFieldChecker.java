package ch.judos.generic.games.pathsearch;

/**
 * checks whether a field on the map is free for some unit such that the unit
 * doesn't block itself
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public class UnitOnMapFreeFieldChecker extends FreeFieldCheckAdapter {

	private Obstacle unit;

	/**
	 * @param m
	 * @param unit
	 */
	public UnitOnMapFreeFieldChecker(GridMap m, Obstacle unit) {
		super(m);
		this.unit = unit;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.FreeFieldChecker#isFree(ch.judos.generic.games.pathsearch.WayPoint)
	 */
	@Override
	public boolean isFree(WayPoint p) {
		if (isOwnUnit(p) || this.map.isFree(p))
			return true;
		return false;
	}

	/**
	 * @param wp
	 * @return whether the waypoint is occupied by the unit
	 */
	protected boolean isOwnUnit(WayPoint wp) {
		for (WayPoint f : this.unit.getFields()) {
			if (f.equals(wp))
				return true;
		}
		return false;
	}

}
