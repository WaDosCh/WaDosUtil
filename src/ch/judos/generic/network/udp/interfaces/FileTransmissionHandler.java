package ch.judos.generic.network.udp.interfaces;

import java.io.File;
import ch.judos.generic.network.udp.model.FileDescription;

/**
 * @since 14.07.2013
 * @author Julian Schelker
 */
public interface FileTransmissionHandler {

	/**
	 * for every incoming transfer the fileTransmissionHandler must decide to
	 * choose a file location where to store the incoming transmission. It can
	 * also decide to return null and therefore decline the transfer of the file
	 * 
	 * @param fd
	 *            the description of the incoming file
	 * @return the target save location or null if the transfer should be
	 *         declined
	 */
	public File requestFileTransmission(FileDescription fd);

	/**
	 * for every incoming transfer a new fileListener is queried, the listener
	 * is updated over the progress and success of the incoming transfer.<br>
	 * 
	 * @param fd
	 *            the description of the incoming file
	 * @return
	 */
	public UdpFileTransferListener requestTransferFileListener(FileDescription fd);

}
