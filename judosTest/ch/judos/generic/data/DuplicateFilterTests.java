package ch.judos.generic.data;

import java.text.NumberFormat;

import junit.framework.TestCase;

/**
 * @since 07.07.2013
 * @author Julian Schelker
 */
public class DuplicateFilterTests extends TestCase {

	private static final boolean SILENT = true;

	private void printMem(int i) {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();
		long allocatedMemory = runtime.totalMemory();
		if (!SILENT)
			System.out.println("allocated memory: " + format.format(allocatedMemory / 1024)
				+ "  (" + format.format(i) + ")");
	}

	public void test() {
		DuplicateFilter x = new DuplicateFilter();

		assertTrue(x.hit(0));
		assertTrue(x.hit(1));
		assertFalse(x.hit(1));
		x = new DuplicateFilter();
		printMem(0);
		long ms = System.currentTimeMillis();
		for (int i = 1; i < 100000; i++) {
			if (i % 1000000 != 0) {
				x.hit(i);
			}
			else {
				assertTrue(x.hit(i));
				if (System.currentTimeMillis() - ms > 1000) {
					printMem(i);
					ms = System.currentTimeMillis();
					for (int nr = 0; nr < 10; nr++) {
						int j = RandomJS.getInt(0, i - 1);
						assertFalse(x.hit(j));
					}
				}
			}

		}

		printMem(-1);

	}
}
