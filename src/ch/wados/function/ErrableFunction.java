package ch.wados.function;

/**
 * Functional interface for a function (X =&gt; Y), that is allowed to throw
 * exceptions
 * 
 * @author Andreas Wälchli
 * @since 1.1, 2015-05-11
 *
 * @param <X>
 *            the input type
 * @param <Y>
 *            the return type
 */
@FunctionalInterface
public interface ErrableFunction<X, Y> {

	/**
	 * Applies the function to the input value
	 * 
	 * @param x
	 *            the input value
	 * @return the result
	 * @throws Exception
	 *             can be throws if required
	 */
	Y apply(X x) throws Exception;

}
