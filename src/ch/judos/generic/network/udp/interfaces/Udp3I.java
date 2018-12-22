package ch.judos.generic.network.udp.interfaces;

import java.io.IOException;
import java.net.InetSocketAddress;
import ch.judos.generic.network.udp.ConnectionIssueListener;

/**
 * handles big objects
 * 
 * @since 11.07.2013
 * @author Julian Schelker
 */
public interface Udp3I {

	public abstract void addListener(Layer3Listener listener);

	/**
	 * closes the connection and interrupts all threads
	 */
	public void dispose();

	/**
	 * @return the local port this udp is listening to
	 */
	public abstract int getLocalPort();

	/**
	 * @return the maximal amount of bytes that can be sent at once (big packets
	 *         are forced to be confirmed, because it is very likely that one
	 *         part is lost)
	 */
	public int getMaxConfirmedPacketSize();

	/**
	 * @return the maximal amount of bytes that can be sent at once without
	 *         splitting the packet into parts. This is also the biggest
	 *         possible unconfirmed packet, since confirmation for big packets
	 *         is forced.
	 */
	public int getMaxUnsplitPacketSize();

	public abstract void removeListener(Layer3Listener listener);

	/**
	 * @param type
	 *            1-63
	 * @param data
	 *            content of the message
	 * @param confirmation
	 *            turned on automatically for big packets (where the content
	 *            needs to be split)
	 * @param to
	 *            receivers address
	 * @throws IOException
	 */
	public abstract void sendDataTo(int type, byte[] data, boolean confirmation,
		InetSocketAddress to) throws IOException;

	public void addConnectionIssueListener(ConnectionIssueListener c);

	public void removeConnectionIssueListener(ConnectionIssueListener c);
}