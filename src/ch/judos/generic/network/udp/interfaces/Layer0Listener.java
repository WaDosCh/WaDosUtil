package ch.judos.generic.network.udp.interfaces;

import java.net.InetSocketAddress;

/**
 * @since 04.07.2013
 * @author Julian Schelker
 */
public interface Layer0Listener {

	void receivedFrom(byte[] data, InetSocketAddress socketAddress);

}
