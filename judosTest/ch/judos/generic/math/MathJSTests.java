package ch.judos.generic.math;

import static org.hamcrest.Matchers.greaterThan;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;



/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public class MathJSTests {

	@Test
	public void testStdv() {

		ArrayList<Integer> list = new ArrayList<>();
		list.add(0);
		list.add(6);

		double stdv = MathJS.stdv(list);
		
		Assert.assertThat(stdv, greaterThan(0.));

	}

}
