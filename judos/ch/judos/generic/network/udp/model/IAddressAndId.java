package ch.judos.generic.network.udp.model;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class IAddressAndId {

	private InetSocketAddress address;
	private short id;

	public IAddressAndId(short id, InetSocketAddress address) {
		this.id = id;
		this.address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof IAddressAndId))
			return false;
		IAddressAndId ia = (IAddressAndId) obj;
		return ia.id == this.id && ia.address.equals(this.address);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[]{this.id, this.address});
	}

}
