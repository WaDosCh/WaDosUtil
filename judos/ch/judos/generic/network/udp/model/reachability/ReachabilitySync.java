package ch.judos.generic.network.udp.model.reachability;

import java.net.InetSocketAddress;
import ch.judos.generic.network.udp.interfaces.ReachabilityListener;
import ch.judos.generic.network.udp.interfaces.Udp4I;

/**
 * @since 02.06.2015
 * @author Julian Schelker
 */
public class ReachabilitySync implements ReachabilityListener {

	private int timeout;
	private Reachability result;

	public ReachabilitySync(InetSocketAddress target, int timeout, Udp4I udp4) {
		this.timeout = timeout;
		this.result = new Reachability(false, 0);
		udp4.checkReachability(target, this);
	}

	@Override
	public void connectionActive(InetSocketAddress target, int pingMS) {
		this.result = new Reachability(true, pingMS);
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public void connectionTimedOut(InetSocketAddress target) {
		this.result = new Reachability(false, this.timeout);
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public int getTimeoutMS() {
		return this.timeout;
	}

	public Reachability waitUntilDone() {
		try {
			synchronized (this) {
				this.wait();
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.result;
	}

}
