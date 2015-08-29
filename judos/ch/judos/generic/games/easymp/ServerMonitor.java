package ch.judos.generic.games.easymp;

import ch.judos.generic.games.easymp.api.CommunicatorI;
import ch.judos.generic.games.easymp.msgs.Message;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class ServerMonitor extends Monitor {

	protected ServerMonitor(boolean isServer, CommunicatorI c) {
		super(isServer, c);
	}

	@Override
	protected void redistribute(Message m) {
		// send to all except the source
		this.communicator.sendToAll(m.data, m.source);
	}

}
