package ch.judos.generic.test;

/**
 * @since 24.07.2014
 * @author Julian Schelker
 */
public class AssertionUtils {

	public static void assertNotNil(Object o, String msg) {
		if (o == null)
			throw new RuntimeException(msg);
	}

	public static void assertNotNil(Object o) {
		if (o == null)
			throw new RuntimeException("The param may not be null");
	}

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
