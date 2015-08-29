package ch.judos.generic.network.udp.model.reachability;

/**
 * @since 02.06.2015
 * @author Julian Schelker
 */
public class Reachability {

	private boolean reachable;

	private int pingOrTimeoutMS;

	public Reachability(boolean reachable, int pingOrTimeoutMS) {
		this.reachable = reachable;
		this.pingOrTimeoutMS = pingOrTimeoutMS;
	}

	public boolean isReachable() {
		return this.reachable;
	}

	public int getPingMS() {
		return this.pingOrTimeoutMS;
	}

	public int getTimeoutMS() {
		return this.pingOrTimeoutMS;
	}

}
