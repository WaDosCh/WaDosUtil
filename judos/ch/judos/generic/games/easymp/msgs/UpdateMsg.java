package ch.judos.generic.games.easymp.msgs;

import java.io.Serializable;
import ch.judos.generic.games.easymp.MonitoredObjectStorage;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public abstract class UpdateMsg implements Serializable {

	private static final long serialVersionUID = -3702614265304050303L;

	public abstract void install(MonitoredObjectStorage storage);
}
