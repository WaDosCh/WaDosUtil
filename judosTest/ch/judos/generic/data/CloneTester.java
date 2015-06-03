package ch.judos.generic.data;

import junit.framework.TestCase;
import ch.judos.generic.data.Cloner;
import ch.judos.generic.data.TupleR;

/**
 * @created 28.04.2012
 * @author Julian Schelker
 */
public class CloneTester extends TestCase {

	@SuppressWarnings({"rawtypes", "unchecked", "cast"})
	public void testCloneTuples() throws CloneNotSupportedException {

		TupleR t = new TupleR(2.4f, "asdf");
		TupleR t2 = (TupleR) t.clone();
		assertEquals(t, t2);

		t = new TupleR(2, 4.2);
		t2 = (TupleR) t.clone();
		assertEquals(t, t2);
	}

	public void testCloneArrays() throws CloneNotSupportedException, SecurityException {
		int[][] x = new int[2][2];
		x[0][0] = 42;

		int[][] y = (int[][]) Cloner.tryClone(x);
		assertEquals(x[0][0], y[0][0]);
	}
}
