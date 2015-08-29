package ch.waan.game;

import java.util.function.IntConsumer;

/**
 * High Precision Clock
 * 
 * @author Andreas Waelchli (porting and maintenance)
 * @author Julian Schelker (initial version)
 * @version 1.1, 2015-02-27
 */
class HighPrecisionClock {

	private volatile int tps;
	private final IntConsumer task;
	private Thread runner;

	HighPrecisionClock(int tps, IntConsumer task) {
		this.tps = tps;
		this.task = task;
	}

	void start() {
		if (this.runner != null)
			throw new IllegalStateException("Cannot start a running clock");
		this.runner = new Thread(this::run);
		this.runner.start();
	}

	void stop() {
		if (this.runner == null)
			throw new IllegalStateException("cannot stop a halted clock");
		this.runner.interrupt();
		try {
			this.runner.join();
		}
		catch (InterruptedException e) {
			// ignore
		}
		this.runner = null;
	}

	int getTPS() {
		return this.tps;
	}

	void setTPS(int tps) {
		if (tps <= 0)
			throw new IllegalArgumentException("tick rate must be strictly positive. " + tps
				+ " is no legal value");
		this.tps = tps;
	}

	private void run() {
		try {
			long lastFrame = System.nanoTime();
			while (!Thread.currentThread().isInterrupted()) {
				long delay = 1000000000 / this.tps;
				long ns = System.nanoTime();
				if (ns - lastFrame >= delay) {
					int timePassed = (int) ((ns - lastFrame) / 1000000);
					lastFrame = ns;
					this.task.accept(timePassed);
				}
				long remaining = ((System.nanoTime() - lastFrame - delay) / 2 - 100000);
				if (remaining > 0)
					Thread.sleep((int) (remaining / 1000000), (int) (remaining % 1000000));
			}
		}
		catch (InterruptedException e) {
			// interrupt is OK. terminate.
		}
	}
}