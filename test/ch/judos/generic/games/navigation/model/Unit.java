package ch.judos.generic.games.navigation.model;

import ch.judos.generic.games.pathsearch.WayPoint;
import ch.judos.generic.graphics.Drawable;

public interface Unit extends Drawable {

	public void commandTo(WayPoint point);

	public void pauseOrContinue();

	public void stopAndRemoveTarget();
}
