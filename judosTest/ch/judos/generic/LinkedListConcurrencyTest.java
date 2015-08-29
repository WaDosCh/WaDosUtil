package ch.judos.generic;

import static org.junit.Assert.fail;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;

/**
 * with more than one thread adding elements to the linkedlist the thread which
 * removes the elements gets an NoSuchElementException. <br>
 * The test verifies the statement above.
 * 
 * @created 25.08.2012
 * @author Julian Schelker
 */
public class LinkedListConcurrencyTest {

	private LinkedList<Integer> list;

	@Before
	public void setUp() throws Exception {
		this.list = new LinkedList<>();
	}

	@Test
	public void testConc() throws Exception {
		try {
			int s = 3;
			Worker[] w = new Worker[s];
			for (int i = 0; i < s; i++)
				w[i] = new Worker(this.list, i == 0);
			for (Thread t : w)
				t.start();

			for (Thread t : w)
				t.join();
			for (Worker t : w)
				if (t.failed != null)
					throw t.failed;
		}
		catch (Exception e) {
			return;
		}
		fail();
	}

	private class Worker extends Thread {

		private LinkedList<Integer> l;
		private boolean add;
		public Exception failed;

		public Worker(LinkedList<Integer> l, boolean add) {
			this.l = l;
			this.add = add;
			this.failed = null;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 1000000; i++) {
					if (this.add)
						this.l.addLast(1);
					else {
						if (this.l.size() > 0)
							this.l.removeFirst();
					}
				}
			}
			catch (Exception e) {
				this.failed = e;
			}
		}
	}

}
