package ch.judos.generic.math.operators;

/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public abstract class BinaryOperator {

	public abstract double calculate(double a, double b);

	public UnaryOperator bindArgumentA(final double a) {
		return new UnaryOperator() {
			@Override
			public double calculate(double b) {
				return BinaryOperator.this.calculate(a, b);
			}
		};
	}

	public UnaryOperator bindArgumentB(final double b) {
		return new UnaryOperator() {
			@Override
			public double calculate(double a) {
				return BinaryOperator.this.calculate(a, b);
			}
		};
	}
}
