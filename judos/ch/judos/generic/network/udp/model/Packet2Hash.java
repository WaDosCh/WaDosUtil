package ch.judos.generic.network.udp.model;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @since 05.07.2013
 * @author Julian Schelker
 */
public class Packet2Hash {

	protected InetSocketAddress dest;
	protected int nr;

	public Packet2Hash() {
		super();
	}

	public Packet2Hash(InetSocketAddress dest, int nr) {
		this.dest = dest;
		this.nr = nr;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Packet2Hash))
			return false;
		Packet2Hash p2 = (Packet2Hash) obj;
		return this.nr == p2.nr && this.dest == p2.dest;
	}

	/**
	 * @return the dest
	 */
	public InetSocketAddress getDestination() {
		return this.dest;
	}

	/**
	 * allows Udp2 to memorize confirmed packets and remove them from the resend
	 * queue
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[]{this.nr, this.dest.hashCode()});
	}

}