package ch.judos.generic.network.udp;

import java.net.InetSocketAddress;

/**
 * @since 04.08.2013
 * @author Julian Schelker
 */
public interface ConnectionIssueListener {

	void connectionIsBroken(InetSocketAddress destination);

	void connectionReconnected(InetSocketAddress from);

}
