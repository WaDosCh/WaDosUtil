package ch.judos.generic.control.concurrency;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class ProgressThread extends Thread implements ProgressTrackable {

	protected LinkedBlockingQueue<Runnable> tasks;
	protected int executedOperations;
	protected boolean shouldRun;

	public ProgressThread(String name) {
		super(name);
		this.tasks = new LinkedBlockingQueue<>();
		this.executedOperations = 0;
		this.shouldRun = true;
	}

	@Override
	public void run() {
		while (this.shouldRun) {
			Runnable r = this.tasks.poll();
			if (r == null)
				break;
			r.run();
			this.executedOperations++;
		}
	}

	@Override
	public double getProgress() {
		int estimatedRemainingOperations = this.tasks.size();
		int total = this.executedOperations + estimatedRemainingOperations;
		return (double) this.executedOperations / total;
	}
}
