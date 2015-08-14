package ch.judos.generic.network.udp.interfaces;

import java.io.IOException;
import java.net.InetSocketAddress;
import ch.judos.generic.network.udp.ConnectionIssueListener;

/**
 * handles confirmation of messages, with the confirmation flag set to true, a
 * message will be resent until a confirmation for it is received
 * 
 * @since 08.07.2013
 * @author Julian Schelker
 */
public interface Udp2I {

	public void addListener(Layer2Listener listener);

	public void dispose();

	/**
	 * @return the local port this udp is listening on
	 */
	public int getLocalPort();

	public int getMaxPacketSize();

	public void removeListener(Layer2Listener listener);

	public void sendDataTo(int type, byte[] data, boolean confirmation,
		InetSocketAddress dest) throws IOException;
	
	public void addConnectionIssueListener(ConnectionIssueListener c);
	
	public void removeConnectionIssueListener(ConnectionIssueListener c);

}