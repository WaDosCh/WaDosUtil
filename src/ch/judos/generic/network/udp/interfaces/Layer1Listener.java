package ch.judos.generic.network.udp.interfaces;

import java.net.InetSocketAddress;

/**
 * @since 04.07.2013
 * @author Julian Schelker
 */
public interface Layer1Listener {

	void receivedMsg(int type, byte[] packetData, InetSocketAddress from);

}
