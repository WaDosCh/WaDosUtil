package ch.judos.generic.test;

/**
 * @since 24.07.2014
 * @author Julian Schelker
 */
public class AssertionUtils {

	/**
	 * @return whether assertions are enabled by the JVM
	 */
	public static boolean isAssertionsEnabled() {
		boolean assertionsActive = false;
		try {
			assert (false);
		}
		catch (AssertionError e) {
			assertionsActive = true;
		}
		return assertionsActive;
	}

	/**
	 * will throw a RuntimeException when assertions are not enabled
	 */
	public static void checkAssertionsEnabled() {
		if (!isAssertionsEnabled())
			throw new RuntimeException(
				"Assertions not enabled by the VM. Add \"-ea\" as argument.");
	}
}
