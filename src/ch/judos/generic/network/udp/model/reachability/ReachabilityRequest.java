package ch.judos.generic.network.udp.model.reachability;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;

import ch.judos.generic.data.RandomJS;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class ReachabilityRequest implements Serializable {
	private static final long serialVersionUID = 4854504538462295479L;
	private int id;
	private transient int ping;
	private long sent;
	private InetSocketAddress target;

	public ReachabilityRequest(InetSocketAddress target) {
		this.id = RandomJS.getInt(Integer.MAX_VALUE - 1);
		this.sent = System.currentTimeMillis();
		this.target = target;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReachabilityRequest))
			return false;
		ReachabilityRequest r = (ReachabilityRequest) obj;
		return this.id == r.id && this.sent == r.sent && this.target.equals(r.target);
	}

	public int getPingMS() {
		return this.ping;
	}

	public InetSocketAddress getTarget() {
		return this.target;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[]{this.id, this.sent, this.target});
	}

	public void responseReceived() {
		this.ping = (int) (System.currentTimeMillis() - this.sent);
	}
}
