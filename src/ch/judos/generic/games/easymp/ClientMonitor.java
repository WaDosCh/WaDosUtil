package ch.judos.generic.games.easymp;

import ch.judos.generic.games.easymp.api.CommunicatorI;
import ch.judos.generic.games.easymp.msgs.Message;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class ClientMonitor extends Monitor {

	protected ClientMonitor(CommunicatorI c) {
		super(false, c);
	}

	@Override
	protected void redistribute(Message m) {
		// does not redistribute
	}

}
