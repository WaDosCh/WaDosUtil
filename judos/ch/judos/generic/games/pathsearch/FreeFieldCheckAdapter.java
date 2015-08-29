package ch.judos.generic.games.pathsearch;

import java.util.HashSet;

/**
 * @since 27.07.2013
 * @author Julian Schelker
 */
public abstract class FreeFieldCheckAdapter implements FreeFieldChecker {

	protected GridMap map;

	public FreeFieldCheckAdapter(GridMap map) {
		this.map = map;
	}

	@Override
	public boolean isFree(HashSet<WayPoint> fields) {
		for (WayPoint w : fields)
			if (!isFree(w))
				return false;
		return true;
	}

	@Override
	public abstract boolean isFree(WayPoint p);

	@Override
	public boolean isFreeAndInsideMap(WayPoint p) {
		return this.map.isInsideMap(p) && isFree(p);
	}
}
