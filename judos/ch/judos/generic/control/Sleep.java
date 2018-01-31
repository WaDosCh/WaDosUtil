package ch.judos.generic.control;

public class Sleep {
	public static void sleepWithoutException(int millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// don't care
		}
	}
}
