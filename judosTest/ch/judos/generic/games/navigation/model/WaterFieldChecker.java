package ch.judos.generic.games.navigation.model;

import ch.judos.generic.games.navigation.model.Map.Water;
import ch.judos.generic.games.pathsearch.FreeFieldCheckAdapter;
import ch.judos.generic.games.pathsearch.WayPoint;

/**
 * @since 27.07.2013
 * @author Julian Schelker
 */
public class WaterFieldChecker extends FreeFieldCheckAdapter {

	private Map gMap;

	public WaterFieldChecker(Map map) {
		super(map);
		this.gMap = map;
	}

	@Override
	public boolean isFree(WayPoint p) {
		return this.gMap.floor[p.getX()][p.getY()] instanceof Water && this.gMap.isFree(p);
	}

}
