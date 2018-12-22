package ch.judos.generic.network.udp.interfaces;

/**
 * @since 12.07.2013
 * @author Julian Schelker
 */
public interface UdpFileTransferListener {

	/**
	 * @return the number of milliseconds between updates of progress
	 */
	public int getUpdateEveryMS();

	/**
	 * this method is called when the file transmission was accepted and has
	 * just started
	 */
	public void transmissionAcceptedAndStarted();

	/**
	 * this method is called when the file transmission has succeeded
	 */
	public void transmissionCompleted();

	/**
	 * this method is called when the file transmission is denied and canceled
	 */
	public void transmissionDeniedAndCanceled();

	/**
	 * @param percentage
	 *            0-100% progress
	 * @param avgSpeed
	 *            measured in bytes/s
	 * @param transmitted
	 *            bytes transmitted
	 * @param total
	 *            total bytes to be transmitted
	 */
	public void transmissionProgress(float percentage, float avgSpeed, long transmitted,
		long total);

}
