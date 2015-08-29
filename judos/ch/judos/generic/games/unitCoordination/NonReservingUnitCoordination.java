package ch.judos.generic.games.unitCoordination;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.TripleW;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.games.pathsearch.*;

/**
 * Coordinates the movement of a unit. Extend this class if you want some object
 * moving around and not alocating any space on the map
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public abstract class NonReservingUnitCoordination extends Unit {

	private FreeFieldChecker checker;
	/**
	 * the final target of the unit, if is is reached the next path will be
	 * calculated until this list is empty
	 */
	protected ArrayList<WayPoint> finalTargets;
	/**
	 * the map to use
	 */
	protected GridMap map;
	/**
	 * current position in pixels on the map
	 */
	protected PointF pos;
	/**
	 * intermediate targets of the path
	 */
	protected ArrayList<WayPoint> targets;

	/**
	 * @param map
	 *            the map to use
	 * @param pos
	 *            the starting field position of the unit
	 * @throws NoFreeSpaceException
	 *             if at the starting field position there's no space
	 */
	public NonReservingUnitCoordination(GridMap map, WayPoint pos) throws NoFreeSpaceException {
		this(map, pos, null);
	}

	/**
	 * @param map
	 *            the map to use
	 * @param pos
	 *            the starting field position of the unit
	 * @param checker
	 *            used to check where this unit can go
	 * @throws NoFreeSpaceException
	 *             if at the starting field position there's no space
	 */
	@SuppressWarnings("all")
	public NonReservingUnitCoordination(GridMap map, WayPoint pos, FreeFieldChecker checker)
		throws NoFreeSpaceException {
		super();
		if (checker == null)
			checker = new UnitOnMapFreeFieldChecker(map, this);
		this.checker = checker;
		if (!map.isFree(pos))
			throw new NoFreeSpaceException("Cannot place unit on occupied field.");
		this.map = map;
		this.pos = map.getPointFromField(pos);
		this.targets = new ArrayList<>();
		this.finalTargets = new ArrayList<>();
	}

	private void calcNewRoute(WayPoint current, WayPoint target) {
		notifyAllListeners(Unit.UNIT_EVENT.ROUTE_RECALC);
		AStarPathSearch ps = new AStarPathSearch();
		Path path = ps.searchWay(current, target, this.checker);
		if (path != null) {
			path = path.getAirWay(this.checker, this.map);
			Iterable<WayPoint> i = DynamicList.getIterableObject(path.getPoints());
			for (WayPoint p : i)
				this.targets.add(new SimpleWayPoint(p.getX(), p.getY()));

		}
		else {
			notifyAllListeners(Unit.UNIT_EVENT.CANT_REACH_TARGET);
		}
	}

	/**
	 * @param toGo
	 * @return newPosition, next grid reached
	 */
	private TripleW<PointF, Boolean, Float> calcNextPosition(float toGo) {
		WayPoint cur = this.targets.get(0);
		PointF target = this.map.getPointFromField(cur);
		PointF position = this.pos.clone();
		boolean reachedTarget = position.approachPoint(target, toGo);
		float remaining = toGo - position.distanceTo(this.pos);
		return new TripleW<>(position, reachedTarget, remaining);
	}

	/**
	 * @param point
	 *            check whether this point collides with any obstacle on the map
	 * @return true if the new position for the unit is allowed
	 */
	protected boolean checkMovementCollidesAndAllocate(PointF point) {
		HashSet<WayPoint> fields = getCornerFields(point);
		return this.checker.isFree(fields);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.unitCoordination.Unit#commandTo(ch.judos.generic.games.pathsearch.WayPoint)
	 */
	@Override
	public void commandTo(WayPoint target) {
		this.targets.clear();
		this.finalTargets.clear();
		this.finalTargets.add(target);
	}

	/**
	 * assumes an object has the size of one field
	 * 
	 * @param point
	 *            some point on the map
	 * @return all waypoints that are occupied by an object the size of one
	 *         field
	 */
	protected HashSet<WayPoint> getCornerFields(PointF point) {
		int size = this.map.getGridSize();
		HashSet<WayPoint> f = new HashSet<>();
		PointF even = new PointF(size / 2 - .5f, size / 2 - .5f);
		PointF odd = new PointF(size / 2 - .5f, -size / 2 + .5f);
		f.add(this.map.getFieldFromPoint(point.add(even)));
		f.add(this.map.getFieldFromPoint(point.add(odd)));
		f.add(this.map.getFieldFromPoint(point.subtract(even)));
		f.add(this.map.getFieldFromPoint(point.subtract(odd)));
		return f;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.pathsearch.Obstacle#getFields()
	 */
	@Override
	protected Set<WayPoint> getFields() {
		return new HashSet<>();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.unitCoordination.Unit#getPosition()
	 */
	@Override
	public PointF getPosition() {
		return this.pos.clone();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.unitCoordination.Unit#getSpeed()
	 */
	@Override
	protected abstract float getSpeed();

	private float goDefinedRoute(float toGo) {
		TripleW<PointF, Boolean, Float> nextPos = calcNextPosition(toGo);

		boolean ok = checkMovementCollidesAndAllocate(nextPos.e0);
		if (ok) {
			this.pos = nextPos.e0;
			if (nextPos.e1)
				this.targets.remove(0);
			return nextPos.e2;
		}// else {
		this.targets.clear();
		return 0;
		// }
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.unitCoordination.Unit#stopAndRemoveTarget()
	 */
	@Override
	public void stopAndRemoveTarget() {
		this.targets.clear();
		this.finalTargets.clear();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.unitCoordination.Unit#update()
	 */
	@Override
	public void update() {
		float toGo = getSpeed();
		while (toGo > 0.1f && !this.targets.isEmpty()) {
			toGo = goDefinedRoute(toGo);
		}
		if (this.targets.isEmpty() && !this.finalTargets.isEmpty()) {
			WayPoint current = this.map.getFieldFromPoint(this.pos);
			// if the unit reached the target field and if the unit is also in
			// the center of the target field
			if (current.equals(this.finalTargets.get(0))
				&& this.map.getPointFromField(current).equals(this.pos)) {
				this.finalTargets.remove(0);
				notifyAllListeners(Unit.UNIT_EVENT.TARGET_REACHED);
			}
			else
				calcNewRoute(current, this.finalTargets.get(0));
		}
	}
}
