package ch.waan.util;

import java.util.function.BooleanSupplier;

import org.eclipse.jdt.annotation.NonNull;

/**
 * An adapter object designed for detecting raising edges on boolean values to
 * allow for simpler client handling.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-28
 */
public final class BooleanEdge {

	private final @NonNull BooleanSupplier expression;

	private boolean lastValue = false;

	/**
	 * creates a new edge adapter for the given expression.
	 * 
	 * @param expression
	 *            the expression to observe
	 */
	public BooleanEdge(@NonNull BooleanSupplier expression) {
		this.expression = expression;
	}

	/**
	 * Updates the edge adapter
	 * 
	 * @return {@code true} if a raising edge was detected. {@code false}
	 *         otherwise.
	 * @throws RuntimeException
	 *             if the observed expression yields an exception
	 */
	public boolean update() {
		try {
			if (this.lastValue != this.expression.getAsBoolean()) {
				this.lastValue = !this.lastValue;
				return this.lastValue;
			}
			return false;
		}
		catch (RuntimeException ex) {
			throw new RuntimeException(ex);
		}
	}

}
