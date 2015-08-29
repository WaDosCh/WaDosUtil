package ch.waan.util;

import java.util.NoSuchElementException;

import ch.waan.function.ErrableFunction;

class ErrorResult<T> extends Result<T> {

	private final Exception e;

	ErrorResult(Exception e) {
		this.e = e;
	}

	@Override
	public T get() {
		throw new NoSuchElementException("erroneous result: " + this.e);
	}

	@Override
	public Exception exception() {
		return this.e;
	}

	@Override
	public boolean isPresent() {
		return false;
	}

	@Override
	public boolean isErroneous() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public <E> Result<E> map(ErrableFunction<T, E> mapper) {
		return new ErrorResult<>(this.e);
	}

	@Override
	public <E> Result<E> flatMap(ErrableFunction<T, Result<E>> mapper) {
		return new ErrorResult<>(this.e);
	}

	@Override
	public Result<T> mapException(ErrableFunction<Exception, T> mapper) {
		try {
			T result = mapper.apply(this.e);
			if (result != null)
				return new ValueResult<>(result);
			return new EmptyResult<>();
		}
		catch (Exception ex) {
			return new ErrorResult<>(ex);
		}
	}

	@Override
	public Result<T> flatMapException(ErrableFunction<Exception, Result<T>> mapper) {
		try {
			return mapper.apply(this.e);
		}
		catch (Exception ex) {
			return new ErrorResult<>(ex);
		}
	}

	@Override
	public String toString() {
		return "ERROR( " + this.e.toString() + " @ " + this.e.getStackTrace()[0] + " )";
	}
}
