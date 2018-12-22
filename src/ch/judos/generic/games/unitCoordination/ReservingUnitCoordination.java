package ch.judos.generic.games.unitCoordination;

import java.util.HashSet;
import java.util.Set;

import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.games.pathsearch.FreeFieldChecker;
import ch.judos.generic.games.pathsearch.WayPoint;

/**
 * Coordinates the movement of a unit. Extend this class if you want some object
 * moving around on the map and alocating space on the map
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public abstract class ReservingUnitCoordination extends NonReservingUnitCoordination {

	/**
	 * the map
	 */
	protected SpaceGridMap bmap;
	private HashSet<WayPoint> occupiedFields;

	/**
	 * @param map
	 *            the map
	 * @param pos
	 *            initial position of the object
	 * @throws NoFreeSpaceException
	 *             thrown if the initial position is already occupied
	 */
	public ReservingUnitCoordination(SpaceGridMap map, WayPoint pos)
		throws NoFreeSpaceException {
		this(map, pos, null);
	}

	/**
	 * @param map
	 *            the map
	 * @param pos
	 *            initial position of the object
	 * @param checker
	 *            used to check where this unit can go
	 * @throws NoFreeSpaceException
	 *             thrown if the initial position is already occupied
	 */
	public ReservingUnitCoordination(SpaceGridMap map, WayPoint pos, FreeFieldChecker checker)
		throws NoFreeSpaceException {
		super(map, pos, checker);
		this.bmap = map;
		if (!this.bmap.reserveFields(getFields()))
			throw new NoFreeSpaceException("Can't create unit on occupied space");

		this.occupiedFields = new HashSet<>();
		this.occupiedFields.addAll(getFields());
	}

	/**
	 * @param point
	 *            check whether this point collides with any obstacle on the map
	 * @return true if the new position for the unit is allowed
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected boolean checkMovementCollidesAndAllocate(PointF point) {
		HashSet<WayPoint> fields = getCornerFields(point);

		HashSet<WayPoint> newFields = (HashSet<WayPoint>) fields.clone();
		newFields.removeAll(this.occupiedFields);

		if (this.bmap.reserveFields(newFields)) {
			this.occupiedFields.addAll(newFields);
			HashSet<WayPoint> leftFields = (HashSet<WayPoint>) this.occupiedFields.clone();
			leftFields.removeAll(fields);
			this.bmap.freeFields(leftFields);
			this.occupiedFields.removeAll(leftFields);
			return true;
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.games.unitCoordination.NonReservingUnitCoordination#getFields()
	 */
	@Override
	protected Set<WayPoint> getFields() {
		return getCornerFields(this.pos);
	}
}
