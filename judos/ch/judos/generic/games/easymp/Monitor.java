package ch.judos.generic.games.easymp;

import java.util.HashSet;
import ch.judos.generic.games.easymp.api.CommunicatorI;
import ch.judos.generic.games.easymp.msgs.Message;
import ch.judos.generic.games.easymp.msgs.ObjectUpdateMsg;
import ch.judos.generic.games.easymp.msgs.UpdateMsg;
import ch.judos.generic.games.easymp.test.Player;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public abstract class Monitor {

	protected boolean isServer;
	protected CommunicatorI communicator;

	protected MonitoredObjectStorage storage;

	private HashSet<Object> updates;
	private static Monitor instance;

	public static void initializeServer(CommunicatorI c) {
		if (instance != null)
			throw new RuntimeException("Monitor was already initialized");
		instance = new ServerMonitor(true, c);
	}

	public static void initializeClient(CommunicatorI c) {
		if (instance != null)
			throw new RuntimeException("Monitor was already initialized");
		instance = new ClientMonitor(false, c);
	}

	public static Monitor getMonitor() {
		if (instance == null)
			throw new RuntimeException("Monitor needs to be first initialized");
		return instance;
	}

	protected Monitor(boolean isServer, CommunicatorI c) {
		this.isServer = isServer;
		this.storage = new MonitoredObjectStorage(c.getClientId());
		this.updates = new HashSet<>();
		this.communicator = c;
	}

	/**
	 * @param o
	 */
	public synchronized void addMonitoredObject(Object o) {
		this.storage.addStaticObject(o);
	}

	public void forceUpdate(Object o) {
		this.updates.add(o);
	}

	public void update() {
		Message m;
		while ((m = this.communicator.receive()) != null) {
			receiveUpdate(m);
		}
		sendUpdates();
	}

	private void sendUpdates() {
		for (Object o : this.updates) {
			UpdateMsg up = new ObjectUpdateMsg(o, this.storage);
			this.communicator.sendToAll(up);
		}
		this.updates.clear();
	}

	private void receiveUpdate(Message m) {
		redistribute(m); // eventually send to other clients
		m.data.install(this.storage);
	}

	protected abstract void redistribute(Message m);

	public void syncNewPlayer(Player newClient) {
		for (Object staticObj : this.storage.getStaticObjects()) {
			UpdateMsg m = new ObjectUpdateMsg(staticObj, this.storage);
			this.communicator.send(m, newClient);
		}
	}

}
