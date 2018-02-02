package controller.portforwarding;

import java.net.DatagramSocket;

/**
 * @author Julian Schelker
 * @lastUpdate 23.04.2012
 * 
 */
public interface PortForwarderI {
	
	/**
	 * forward a random port
	 */
	public DatagramSocket tryForwarding() throws Exception;
	
	public DatagramSocket tryForwarding(int externPort) throws Exception;
	
	public boolean isSucceeded();
	
	public int getExternalPort();
	
	public int getInternalPort();
	
	public String getErrorMessage();
	
}
