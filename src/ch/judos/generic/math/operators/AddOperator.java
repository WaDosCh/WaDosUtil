package ch.judos.generic.math.operators;

/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public class AddOperator extends BinaryOperator {

	@Override
	public double calculate(double a, double b) {
		return a + b;
	}

}
