package ch.judos.generic.exception;

@FunctionalInterface
public interface RunnableThrowsException {
	public void run() throws Exception;

	public default void runWithoutRuntimeException() {
		try {
			run();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a lambda and catches the exceptions just to rethrow them at runtime
	 */
	public static void runRethrow(RunnableThrowsException runnable) {
		runnable.runWithoutRuntimeException();
	}
}
