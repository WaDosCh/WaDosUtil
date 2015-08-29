package ch.judos.generic.network.udp.model.reachability;

import java.io.Serializable;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class ReachabilityResponse implements Serializable {

	private static final long serialVersionUID = 2199566573164124889L;
	private ReachabilityRequest rr;

	public ReachabilityResponse(ReachabilityRequest rr) {
		this.rr = rr;
	}

	public ReachabilityRequest getRequest() {
		this.rr.responseReceived();
		return this.rr;
	}

}
