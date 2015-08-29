package ch.waan.util;

import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import ch.waan.function.ErrableFunction;
import ch.waan.function.ErrableSupplier;

/**
 * 
 * Result is designed for thorough result handling.
 * 
 * It exists in 3 different possible states: <br/>
 * <ol>
 * <li>a Value Result: This represents the main result. It contains a non-null
 * object
 * <li>an Empty (null) Result: This represents "no result". It is the
 * alternative for returning null
 * <li>an Error Result: This represents a failure and contains the exception
 * that would otherwise be thrown
 * </ol>
 * 
 * All of these result types can be interacted with. They can also be used for
 * further processing using a functional code style.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-11
 *
 * @param <T>
 *            the result type
 */
public abstract class Result<T> {

	Result() {
	}

	/**
	 * Returns an empty result
	 * 
	 * @return an empty result
	 */
	public static <T> Result<T> empty() {
		return new EmptyResult<>();
	}

	/**
	 * Wraps a non-null value into a value result.
	 * 
	 * If the value is null, an error result with a NullPointerException is
	 * returned.
	 * 
	 * @param value
	 *            the value to wrap
	 * @return the result containing the value
	 */
	public static <T> Result<T> of(T value) {
		return Result.eval(() -> {
			Objects.requireNonNull(value);
			return value;
		});
	}

	/**
	 * Wraps a value into a value result. Null values are allowed.
	 * 
	 * If the value is null, an empty result is returned instead of the value
	 * result.
	 * 
	 * @param value
	 *            the value to wrap
	 * @return the result containing the value
	 */
	public static <T> Result<T> ofNullable(T value) {
		return value == null ? new EmptyResult<>() : new ValueResult<>(value);
	}

	/**
	 * Wraps an exception into an error result. Null values are allowed, but
	 * yield a NullPointerException wrapped inside the result.
	 * 
	 * @param e
	 *            the exception to wrap
	 * @return the result containing the exception
	 */
	public static <T> Result<T> ofException(Exception e) {
		return Result.of(e).flatMap(ex -> new ErrorResult<>(ex));
	}

	/**
	 * Evaluates a given expression and returns a result according to the
	 * expression result. Any exceptions thrown by the expression will be
	 * captured and wrapped into an error result. Null return values yield empty
	 * results, and non-null values yield value results.
	 * 
	 * @param supplier
	 *            the supplier to evaluate
	 * @return the result of the supplier
	 */
	public static <T> Result<T> eval(ErrableSupplier<T> supplier) {
		try {
			return Result.ofNullable(supplier.get());
		}
		catch (Exception e) {
			return new ErrorResult<>(e);
		}
	}

	/**
	 * Returns the value contained in the result
	 * 
	 * @return the value
	 * @throws NoSuchElementException
	 *             if the result is no value result
	 */
	public abstract T get();

	/**
	 * Returns the exception contained in the result
	 * 
	 * @return the exception
	 * @throws NoSuchElementException
	 *             if the result is no error result
	 */
	public abstract Exception exception();

	/**
	 * Indicates if a value is present. This is an identifier for value results
	 * 
	 * @return {@code true} iff the result is a value result
	 */
	public abstract boolean isPresent();

	/**
	 * Indicates if the result is an error result.
	 * 
	 * @return {@code true} iff the result is an error result
	 */
	public abstract boolean isErroneous();

	/**
	 * Indicates if the result is an empty result.
	 * 
	 * @return {@code true} iff the result is an empty result
	 */
	public abstract boolean isEmpty();

	/**
	 * Applies the mapping function to the value of the result (if it exists).
	 * 
	 * @param mapper
	 *            the mapping function
	 * @return a result containing the result of the mapping operation
	 */
	public abstract <E> Result<E> map(ErrableFunction<T, E> mapper);

	/**
	 * Applies the mapping function to the value of the result and returns the
	 * result returned by the mapping function. If the mapping function throws
	 * an exception, it is captured, wrapped and returned instead.
	 * 
	 * @param mapper
	 *            the mapping function
	 * @return the result of the mapping function
	 */
	public abstract <E> Result<E> flatMap(ErrableFunction<T, Result<E>> mapper);

	/**
	 * Applies the mapping function to the exception.
	 * 
	 * @param mapper
	 *            the mapping function
	 * @return a result containing the result of the mapping function
	 */
	public abstract Result<T> mapException(ErrableFunction<Exception, T> mapper);

	/**
	 * Applies the mapping function to the exception and returns the result
	 * returned by it.
	 * 
	 * @param mapper
	 *            the mapping function
	 * @return the result of the mapping function
	 */
	public abstract Result<T> flatMapException(ErrableFunction<Exception, Result<T>> mapper);

	/**
	 * Evaluates the consumer with the value if it exists
	 * 
	 * @param consumer
	 *            the consumer
	 */
	public void ifPresent(Consumer<T> consumer) {
		if (this.isPresent())
			consumer.accept(this.get());
	}

	/**
	 * Evaluates the consumer with the exception if it exists
	 * 
	 * @param consumer
	 *            the consumer
	 */
	public void ifErroneous(Consumer<Exception> consumer) {
		if (this.isErroneous())
			consumer.accept(this.exception());
	}

	/**
	 * Invokes the runnable if the result is empty
	 * 
	 * @param runner
	 *            the runner
	 */
	public void ifEmpty(Runnable runner) {
		if (this.isEmpty())
			runner.run();
	}

	/**
	 * Returns the value if it exists or {@code null} otherwise
	 * 
	 * @return the value or null
	 */
	public @Nullable T orNull() {
		return this.isPresent() ? this.get() : null;
	}
}
