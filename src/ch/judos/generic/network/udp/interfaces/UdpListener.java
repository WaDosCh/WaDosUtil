package ch.judos.generic.network.udp.interfaces;

import java.net.InetSocketAddress;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public interface UdpListener {
	/**
	 * @param source
	 *            corresponds to the Udp Object that notifies the listener
	 * @param from
	 *            the socketAddress where the udpMessage came from
	 * @param data
	 *            content of the message
	 */
	public void receiveMsg(Object source, InetSocketAddress from, Object data);
}
