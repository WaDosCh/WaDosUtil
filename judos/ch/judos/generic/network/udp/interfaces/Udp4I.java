package ch.judos.generic.network.udp.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.model.reachability.Reachability;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public interface Udp4I {

	/**
	 * add a listener to receive raw byte data
	 * 
	 * @param u
	 */
	public void addDataListener(UdpListener u);

	/**
	 * add some listener to receive objects from the network
	 * 
	 * @param u
	 */
	public void addObjectListener(UdpListener u);

	/**
	 * check whether a target client is reachable and runs the UdpLib as well
	 * 
	 * @param target
	 * @param listener
	 *            receives the updates about the connection
	 */
	public void
		checkReachability(InetSocketAddress target, ReachabilityListener listener);
	
	/**
	 * @param target
	 * @param timeoutMs milliseconds to wait before returning
	 * @return the reachability of the target
	 */
	public Reachability getReachability(InetSocketAddress target, int timeoutMs);

	/**
	 * stops all threads of the udpLib and closes the network connection
	 */
	public void dispose();

	/**
	 * returns the local port, this udpLib is listening to
	 * 
	 * @return
	 */
	public int getLocalPort();

	public void removeDataListener(UdpListener u);

	public void removeObjectListener(UdpListener u);

	/**
	 * sends a file to some target client
	 * 
	 * @param file
	 *            the file to be sent
	 * @param description
	 *            of the file or a reason while this file should be sent
	 * @param target
	 *            the target client
	 * @param fileListener
	 *            some listener to be notified when the file is accepted,
	 *            denied, the progress of the transmission and it's success
	 * @throws FileNotFoundException
	 */
	public void sendFileTo(File file, String description, InetSocketAddress target,
		UdpFileTransferListener fileListener) throws FileNotFoundException;

	/**
	 * sends an object with or without confirmation to a target client
	 * 
	 * @param obj
	 *            the object to be sent
	 * @param confirmation
	 *            true if message should be resent until a confirmation is
	 *            received
	 * @param to
	 *            target client
	 * @throws SerializerException
	 *             if the object couldn't be translated into bytes, check that
	 *             your object is serializable
	 * @throws IOException
	 *             if the underlying network gets some exception
	 */
	public void
		sendObjectConfirmTo(Object obj, boolean confirmation, InetSocketAddress to)
			throws SerializerException, IOException;

	/**
	 * sends some byteData with or without confirmation to a target client
	 * 
	 * @param data
	 *            the byte data to be sent
	 * @param confirmation
	 *            true if message should be resent until a confirmation is
	 *            received
	 * @param to
	 *            target client
	 * @throws SerializerException
	 *             if the object couldn't be translated into bytes, check that
	 *             your object is serializable
	 * @throws IOException
	 *             if the underlying network gets some exception
	 */
	public void sendRawDataConfirmTo(byte[] data, boolean confirmation,
		InetSocketAddress to) throws SerializerException, IOException;

	/**
	 * sets the current fileTransmissionHandler, it's responsibilities are to
	 * accept or deny the transmission of files and upon accepting determining a
	 * target location where to create the file that is received
	 * 
	 * @param fileHandler
	 */
	public void setFileHandler(FileTransmissionHandler fileHandler);

}