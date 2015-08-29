package ch.judos.generic.network.udp;

/**
 * @since 05.07.2013
 * @author Julian Schelker
 */
public class UdpConfig {

	/**
	 * turns the debug behaviour on, e.g. Udp2 drops 33% of packages to check
	 * confirmation
	 */
	public static final boolean DEBUG_BEHAVIOUR = false;

	/**
	 * turn on/off debug output from the different layers, works only if
	 * DEBUG_OUTPUT is set to true
	 */
	private static final boolean[] DEBUG_LEVEL = new boolean[]{true, false, false, false, true};

	/**
	 * whether the udp classes should output some debug text
	 */
	public static final boolean DEBUG_OUTPUT = false;

	/**
	 * the first packet which requests confirmation will receive this nr
	 */
	public static final int FIRST_PACKET_NR = 0;

	/**
	 * a request in file transfer is only sent if this many parts are missing<br>
	 * otherwise if any parts are missing a maximal time of REQUEST_MISSING_MS
	 * is spent before a request is sent
	 */
	public static final int MINIMAL_AMOUNT_REQUEST = 25;

	/**
	 * size of one Udp packet to send (including all control data in the packet)
	 */
	public static final int PACKET_SIZE_BYTES = 8192; // 8kb

	/**
	 * report a broken connection (see
	 * RESEND_COUNT_BEFORE_REPORTING_CONNECTION_ISSUE) every X ms. 0 means the
	 * issue is only reported once, until a valid answer is received from the
	 * connection (see connectionListener for Udp2 objects)
	 */
	public static final long REPORT_CONNECTION_ISSUE_EVERY_MS = 0;

	/**
	 * the fileReceiver requests packets that were already requested only after
	 * this timespan or if there is a MINIMAL_AMOUNT_REQUEST of parts missing
	 */
	public static final long REQUEST_MISSING_MS = 100;

	/**
	 * after sending a confirmed packet X times the connection will be reported
	 * as broken
	 */
	public static final int RESEND_COUNT_BEFORE_REPORTING_CONNECTION_ISSUE = 5;

	/**
	 * in filetransfer when the last packet is sent and is lost, no request is
	 * sent from the receiver, so the final packet is automatically resent after
	 * this timeout
	 */
	public static final long RESEND_FINAL_PACKET_MS = 100;

	/**
	 * if no confirmation is received after this time, the packet is sent again
	 * if confirmation is necessary
	 */
	public static final long RESEND_TIMEOUT_MS = 80;

	/**
	 * in file transfer sends this many packets at once before looping through
	 * other fileTransfers and then doing a thread.yield()
	 */
	public static final int SEND_SPEED_PACKETS = 10;

	public static final long RESEND_TIMEOUT_CACHE_CONFIRMATION_MS = 10;

	/**
	 * debug output function, used to display text from UdpLib classes
	 * 
	 * @param string
	 *            message to display
	 */
	public synchronized static void out(String string) {
		if (!DEBUG_OUTPUT)
			return;
		String clName = new Exception().getStackTrace()[1].getClassName();
		String shortName = clName.substring(clName.length() - 4);
		int level;
		try {
			level = Integer.valueOf(shortName.substring(3, 4));
			if (DEBUG_LEVEL[level]) {
				long ms = System.currentTimeMillis();
				System.out.println("T+" + ms % 1000 + " " + shortName + ": " + string);
			}
		}
		catch (Exception e) {
			System.out.println(clName + ": " + string);
		}
	}

}
