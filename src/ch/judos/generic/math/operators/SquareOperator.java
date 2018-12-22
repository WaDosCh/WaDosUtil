package ch.judos.generic.math.operators;

/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public class SquareOperator implements UnaryOperator {

	@Override
	public double calculate(double arg) {
		return Math.pow(arg, 2);
	}

}
