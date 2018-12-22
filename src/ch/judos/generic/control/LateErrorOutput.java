package ch.judos.generic.control;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @since 23.09.2013
 * @author Julian Schelker
 */
public class LateErrorOutput implements Runnable {

	private static LateErrorOutput instance;
	private HashMap<String, Error> errors;
	private boolean running;
	private PriorityQueue<Error> check;
	private Thread thread;

	public static LateErrorOutput getInstance() {
		if (instance == null)
			instance = new LateErrorOutput();
		return instance;
	}

	public LateErrorOutput() {
		this.errors = new HashMap<>();
		this.check = new PriorityQueue<>();
		this.running = false;
	}

	public void addException(String msg) {
		if (!this.errors.containsKey(msg)) {
			Error e = new Error(msg);
			this.errors.put(msg, e);
			this.check.add(e);
			startThread();
		}
		else {
			this.errors.get(msg).hit();
		}
	}

	private synchronized void startThread() {
		if (!this.running) {
			this.thread = new Thread(this, "LateErrorOutput");
			this.thread.start();
			this.running = true;
		}
	}

	@Override
	public void run() {
		long wait;
		while (true) {
			Error e = this.check.peek();

			if (e.isDumpable()) {
				e.dump();
				this.check.poll();
				if (this.check.isEmpty())
					break;
				// else
				wait = this.check.peek().getRemainingTime();
			}
			else
				wait = e.getRemainingTime();

			try {
				synchronized (this) {
					wait(wait);
				}
			}
			catch (InterruptedException e1) {
				// do nothing
			}
		}
		this.running = false;
	}

	class Error implements Comparable<Error> {
		String msg;
		long lastMsOccured;
		int count;

		public Error(String msg) {
			this.msg = msg;
			this.count = 1;
			this.lastMsOccured = System.currentTimeMillis();
		}

		public void dump() {
			System.err.println(this.msg + " (" + this.count + " times)");
		}

		public long getDumpTime() {
			return this.lastMsOccured + 100;
		}

		public long getRemainingTime() {
			return getDumpTime() - System.currentTimeMillis();
		}

		public boolean isDumpable() {
			return getRemainingTime() <= 0;
		}

		public void hit() {
			this.count++;
			this.lastMsOccured = System.currentTimeMillis();
		}

		@Override
		public int compareTo(Error arg0) {
			return (int) (this.lastMsOccured - arg0.lastMsOccured);
		}

	}

}
