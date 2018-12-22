package ch.judos.generic.games.unitCoordination;

/**
 * A listener that wants to know about movements of a unit
 * 
 * @since 27.02.2013
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public interface MovementListener {

	/**
	 * called if the unit reached its target
	 * 
	 * @param u
	 *            the unit
	 */
	public void targetReached(Object u);

	/**
	 * called if the unit failed to find a path to the target
	 * 
	 * @param u
	 *            the unit
	 */
	public void cantReachTarget(Object u);

	/**
	 * called if the unit recalculates it's route. This happens usually once at
	 * the beginning of the movement of a unit
	 * 
	 * @param u
	 *            the unit
	 */
	public void routeChange(Object u);
}
