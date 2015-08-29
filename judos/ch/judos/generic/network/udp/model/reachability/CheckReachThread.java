package ch.judos.generic.network.udp.model.reachability;

import java.net.InetSocketAddress;

import ch.judos.generic.network.udp.Udp4;
import ch.judos.generic.network.udp.interfaces.ReachabilityListener;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class CheckReachThread extends Thread {

	private ReachabilityListener listener;
	private boolean received;
	private ReachabilityRequest rr;
	private InetSocketAddress target;
	private Udp4 u;

	public CheckReachThread(Udp4 u, ReachabilityRequest rr, ReachabilityListener listener) {
		this.u = u;
		this.rr = rr;
		this.listener = listener;
		this.target = rr.getTarget();
		this.received = false;
		this.setDaemon(true);
		this.start();
	}

	public void pingReceived() {
		this.received = true;
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			try {
				wait(this.listener.getTimeoutMS());
			}
			catch (InterruptedException e) {
				// nothing, received stays on false
			}
		}
		if (this.received) {
			this.listener.connectionActive(this.target, this.rr.getPingMS());
		}
		else {
			this.listener.connectionTimedOut(this.target);
			this.u.reachabilityReqTimedOut(this.rr);
		}
	}

}
