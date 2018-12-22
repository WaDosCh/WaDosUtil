package ch.judos.generic.network.udp.interfaces;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * handles message type numbers
 * 
 * @since 08.07.2013
 * @author Julian Schelker
 */
public interface Udp1I {

	public void addListener(Layer1Listener listener);

	public void dispose();

	public int getLocalPort();

	public int getMaxPacketSize();

	public void removeListener(Layer1Listener listener);

	public void sendDataTo(int type, byte[] data, InetSocketAddress dest) throws IOException;

}