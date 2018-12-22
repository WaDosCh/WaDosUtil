package ch.judos.generic.network.udp;

/**
 * @since 12.07.2013
 * @author Julian Schelker
 */
public class WaitNotifyTest {
	public static void main(String[] args) {
		new WaitNotifyTest().runExample();
	}

	/**
	 * outputs whether thread was woken up<br>
	 * however it might varify since waking up can occur right when the timeout
	 * occurs
	 */
	public void runExample() {

		boolean wokenUp = false;
		final WaitNotifyTest x = this;

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(499);
				}
				catch (InterruptedException e) {
					// not needed
				}
				synchronized (x) {
					x.notify();
				}
			}
		});
		t.start();

		synchronized (this) {
			try {
				long ms = System.currentTimeMillis();
				this.wait(500);
				wokenUp = System.currentTimeMillis() - ms < 500;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(wokenUp);
	}
}
