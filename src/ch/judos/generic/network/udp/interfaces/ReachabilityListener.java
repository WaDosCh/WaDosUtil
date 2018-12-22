package ch.judos.generic.network.udp.interfaces;

import java.net.InetSocketAddress;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public interface ReachabilityListener {

	/**
	 * called when the connection is active
	 * 
	 * @param target
	 * @param pingMS
	 *            how long the reachabilityRequest took
	 */
	public void connectionActive(InetSocketAddress target, int pingMS);

	/**
	 * called when the connection timed out
	 * 
	 * @param target
	 */
	public void connectionTimedOut(InetSocketAddress target);

	/**
	 * @return the amount of milliseconds to wait before the connection has
	 *         timed out
	 */
	public int getTimeoutMS();

}
