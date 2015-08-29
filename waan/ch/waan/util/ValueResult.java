package ch.waan.util;

import java.util.NoSuchElementException;
import java.util.Objects;

import ch.waan.function.ErrableFunction;

class ValueResult<T> extends Result<T> {

	private final T value;

	ValueResult(T value) {
		Objects.requireNonNull(value);
		this.value = value;
	}

	@Override
	public T get() {
		return this.value;
	}

	@Override
	public Exception exception() {
		throw new NoSuchElementException();
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public boolean isErroneous() {
		return false;
	}

	@Override
	public <E> Result<E> map(ErrableFunction<T, E> mapper) {
		try {
			E result = mapper.apply(this.value);
			if (result != null)
				return new ValueResult<>(result);
			return new EmptyResult<>();
		}
		catch (Exception e) {
			return new ErrorResult<>(e);
		}
	}

	@Override
	public <E> Result<E> flatMap(ErrableFunction<T, Result<E>> mapper) {
		try {
			return mapper.apply(this.value);
		}
		catch (Exception ex) {
			return new ErrorResult<>(ex);
		}
	}

	@Override
	public Result<T> mapException(ErrableFunction<Exception, T> mapper) {
		return this;
	}

	@Override
	public Result<T> flatMapException(ErrableFunction<Exception, Result<T>> mapper) {
		return this;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public String toString() {
		return "VALUE( " + this.value.toString() + " )";
	}

}
