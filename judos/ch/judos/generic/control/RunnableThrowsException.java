package ch.judos.generic.control;

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
}
