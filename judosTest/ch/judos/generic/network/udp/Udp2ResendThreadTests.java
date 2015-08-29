package ch.judos.generic.network.udp;

import java.util.PriorityQueue;

import junit.framework.TestCase;
import ch.judos.generic.data.RandomJS;

/**
 * @since 08.07.2013
 * @author Julian Schelker
 */
public class Udp2ResendThreadTests extends TestCase {

	private static final boolean SILENT = true;

	int failed;
	private PriorityQueue<Long> queue;
	private boolean running;

	@SuppressWarnings("unused")
	private void resend(Long x) {
		this.queue.remove();
		int diff = (int) Math.abs(System.currentTimeMillis() - x);
		if (diff > 0 && !SILENT)
			System.out.println(diff);
		assertTrue(diff < 20);
	}

	protected void runUdp2Thread() throws InterruptedException {
		while (this.running) {
			synchronized (this.queue) {
				if (this.queue.size() == 0)
					this.queue.wait();
				else {
					Long x = this.queue.element();
					if (System.currentTimeMillis() >= x)
						resend(x);
					else
						this.queue.wait(x - System.currentTimeMillis());
				}
			}
		}
	}

	public void testPriorityQueue() throws InterruptedException {

		this.queue = new PriorityQueue<>();
		this.queue.add(System.currentTimeMillis() + 20);
		this.queue.add(System.currentTimeMillis() + 50);
		this.running = true;
		this.failed = 0;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					runUdp2Thread();
				}
				catch (Error e) {
					Udp2ResendThreadTests.this.failed++;
				}
				catch (InterruptedException e) {
					Udp2ResendThreadTests.this.failed++;
				}
			}
		}, "Udp2TestThread");
		t.start();
		if (!SILENT)
			System.out.println("thread started");
		for (int i = 0; i < 200; i++) {
			Thread.sleep(RandomJS.getInt(0, 10));
			synchronized (this.queue) {
				this.queue.add(System.currentTimeMillis() + RandomJS.getInt(0, 100));
				this.queue.notify();
			}
		}
		if (!SILENT)
			System.out.println("all elements added, waiting for queue to empty");
		while (true) {
			synchronized (this.queue) {
				if (this.queue.size() == 0)
					break;
			}
			Thread.yield();
			assertEquals(0, this.failed);
		}
		if (!SILENT)
			System.out.println("set running to false");
		this.running = false;
		synchronized (this.queue) {
			this.queue.notify();
		}
		if (!SILENT)
			System.out.println("joining thread");
		t.join();
		assertEquals(0, this.failed);

	}
}
