package ch.wados.function;

/**
 * Functional interface for suppliers that may throw exceptions
 * 
 * @author Andreas Wälchli
 * @since 1.1, 2015-05-11
 *
 * @param <T>
 *            return type
 */
@FunctionalInterface
public interface ErrableSupplier<T> {

	/**
	 * Returns a value
	 * 
	 * @return the return value
	 * @throws Exception
	 *             can be thrown if required
	 */
	T get() throws Exception;

}
