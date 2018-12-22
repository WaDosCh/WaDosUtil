package ch.judos.generic.network.udp;

import ch.judos.generic.network.udp.interfaces.Udp3I;
import ch.judos.generic.network.upnp.UdpPortForwarder;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class Udp4Forwarded extends Udp4 {

	protected int externalPort;

	public Udp4Forwarded(Udp3I u, int externalPort) {
		super(u);
		this.externalPort = externalPort;
	}

	@Override
	public void dispose() {
		super.dispose();
		UdpPortForwarder.removePortMapping(this.externalPort);
	}

	/**
	 * @return the externalPort according to the port mapping that was made
	 */
	public int getExternalPort() {
		return this.externalPort;
	}

}
