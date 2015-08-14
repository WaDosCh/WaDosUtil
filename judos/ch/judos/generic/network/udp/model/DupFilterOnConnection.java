package ch.judos.generic.network.udp.model;

import java.net.InetSocketAddress;
import java.util.HashMap;
import ch.judos.generic.data.DuplicateFilter;

/**
 * @since 08.07.2013
 * @author Julian Schelker
 */
public class DupFilterOnConnection {
	private HashMap<InetSocketAddress, DuplicateFilter>	clients;

	public DupFilterOnConnection() {
		this.clients = new HashMap<>();
	}
	
	public synchronized void resetForConnection(InetSocketAddress addr) {
		this.clients.remove(addr);
	}

	public synchronized boolean check(InetSocketAddress addr, int index) {
		DuplicateFilter d = this.clients.get(addr);
		if (d == null) {
			d = new DuplicateFilter();
			this.clients.put(addr, d);
		}
		return d.check(index);
	}

	public synchronized boolean hit(InetSocketAddress addr, int index) {
		DuplicateFilter d = this.clients.get(addr);
		if (d == null) {
			d = new DuplicateFilter();
			this.clients.put(addr, d);
		}
		return d.hit(index);
	}
}
