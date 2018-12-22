package ch.judos.generic.games.pathsearch;

import java.util.Set;

/**
 * @since 24.02.2013
 * @author Julian Schelker
 * @version 1.01 / 24.02.2013
 */
public abstract class Obstacle {

	/**
	 * @return the fields that are reserved for this unit
	 */
	protected abstract Set<WayPoint> getFields();
}
