package ch.judos.generic.games.unitCoordination;

import java.util.Arrays;
import java.util.Set;

import ch.judos.generic.games.pathsearch.GridMap;
import ch.judos.generic.games.pathsearch.WayPoint;

/**
 * defines the basic behaviour of a GridMap. Any fields can be reserved (once)
 * and be released again
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public abstract class SpaceGridMap extends GridMap {

	/**
	 * how many fields the map contains (width)
	 */
	protected int gridWidth;
	/**
	 * how many fields the map contains (height)
	 */
	protected int gridHeight;
	/**
	 * the array storing which fields are free
	 */
	protected boolean[][] fieldFree;

	/**
	 * @param gridWidth
	 *            the width of the map
	 * @param gridHeight
	 *            the height of the map
	 */
	public SpaceGridMap(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.fieldFree = new boolean[gridWidth][gridHeight];
		for (int x = 0; x < gridWidth; x++)
			Arrays.fill(this.fieldFree[x], true);
	}

	/**
	 * frees space on the map, this should only be called from an object which
	 * occupied these fields
	 * 
	 * @param fields
	 */
	public synchronized void freeFields(Set<WayPoint> fields) {
		for (WayPoint f : fields)
			this.fieldFree[f.getX()][f.getY()] = true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.GridMap#getGridSize()
	 */
	@Override
	public abstract int getGridSize();

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.GridMap#getHeight()
	 */
	@Override
	public int getHeight() {
		return this.gridHeight;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.GridMap#getWidth()
	 */
	@Override
	public int getWidth() {
		return this.gridWidth;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.GridMap#isFree(ch.judos.generic.games.pathsearch.WayPoint)
	 */
	@Override
	public synchronized boolean isFree(WayPoint field) {
		return this.fieldFree[field.getX()][field.getY()];
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.GridMap#isInsideMap(ch.judos.generic.games.pathsearch.WayPoint)
	 */
	@Override
	public boolean isInsideMap(WayPoint field) {
		int x = field.getX();
		int y = field.getY();
		return x >= 0 && x < this.gridWidth && y >= 0 && y < this.gridHeight;
	}

	/**
	 * @param f
	 *            the waypoint to reserve
	 * @return whether the operation succeeded
	 */
	public synchronized boolean reserveField(WayPoint f) {
		if (!this.fieldFree[f.getX()][f.getY()])
			return false;
		this.fieldFree[f.getX()][f.getY()] = false;
		return true;
	}

	/**
	 * @param fields
	 *            tries to reserve fields<br>
	 *            returns false without reserving one if reservation is not
	 *            possible for all fields
	 * @return whether the operation succeeded. Either all given fields are
	 *         reserved or none
	 */
	public synchronized boolean reserveFields(Set<WayPoint> fields) {
		for (WayPoint f : fields) {
			if (!isInsideMap(f))
				return false;
			if (!this.fieldFree[f.getX()][f.getY()])
				return false;
		}
		for (WayPoint f : fields)
			this.fieldFree[f.getX()][f.getY()] = false;
		return true;
	}

}
