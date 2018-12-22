package ch.judos.generic.network.udp;

import java.lang.reflect.Field;
import java.util.PriorityQueue;

import ch.judos.generic.network.udp.model.Packet2ResendConfirmed;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class Udp2Monitor extends Thread {
	private Udp2 u;

	public Udp2Monitor(Udp2 u2) {
		super("Udp2Monitor");
		this.setDaemon(true);
		this.u = u2;
		start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Field f = this.u.getClass().getField("resendPackets");
			while (true) {
				PriorityQueue<Packet2ResendConfirmed> x = (PriorityQueue<Packet2ResendConfirmed>) f
					.get(this.u);
				System.out.println("Packets unconfirmed and queued for resend: " + x.size());
				Thread.sleep(250);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
