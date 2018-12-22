package ch.judos.generic.network.udp.model;

import java.net.InetSocketAddress;

import ch.judos.generic.network.udp.UdpConfig;

/**
 * @since 05.07.2013
 * @author Julian Schelker
 */
public class Packet2ResendConfirmed extends Packet2A {

	int sendCount;

	public Packet2ResendConfirmed(int type, byte[] sendData, InetSocketAddress dest, int nr) {
		super(dest, nr, UdpConfig.RESEND_TIMEOUT_MS);
		this.type = type;
		this.sendData = sendData;
		this.sendCount = 0;
		wasResentNow();
	}

	@Override
	public boolean hasConnectionIssues() {
		return this.sendCount >= UdpConfig.RESEND_COUNT_BEFORE_REPORTING_CONNECTION_ISSUE;
	}

	@Override
	public void wasResentNow() {
		resendIn(UdpConfig.RESEND_TIMEOUT_MS);
		this.sendCount++;
	}

	@Override
	public boolean needsConfirmation() {
		return true;
	}

}
