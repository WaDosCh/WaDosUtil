package ch.judos.generic.network.udp.model;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import ch.judos.generic.network.udp.UdpConfig;

/**
 * @since 29.09.2013
 * @author Julian Schelker
 */
public class Packet2CacheConfirmation extends Packet2A {

	private ArrayList<Integer> confirms;

	public Packet2CacheConfirmation(InetSocketAddress dest, int numberToConfirm) {
		// numberToConfirm is used as identifier
		super(dest, numberToConfirm, UdpConfig.RESEND_TIMEOUT_CACHE_CONFIRMATION_MS);
		this.confirms = new ArrayList<>();
		this.confirms.add(numberToConfirm);
	}

	@Override
	public void wasResentNow() {
		// do nothing
	}

	@Override
	public boolean hasConnectionIssues() {
		return false;
	}

	@Override
	public boolean needsConfirmation() {
		return false;
	}

	public void addConfirms(int packetNumber) {
		this.confirms.add(packetNumber);
	}

}
