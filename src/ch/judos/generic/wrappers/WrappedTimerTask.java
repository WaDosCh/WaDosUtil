package ch.judos.generic.wrappers;

import java.util.TimerTask;

/**
 * creating TimerTask usually needs to be done by implementing an anonymous class which is ugly to
 * do inline. since Runnables can be created easily with java8 it is more convenient to create a
 * WrappedTimerTask with an inlined Runnable object.
 * 
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class WrappedTimerTask extends TimerTask {

	private Runnable runnable;

	public WrappedTimerTask(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		this.runnable.run();
	}

}
