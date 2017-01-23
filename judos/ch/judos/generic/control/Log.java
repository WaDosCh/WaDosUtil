package ch.judos.generic.control;

import ch.judos.generic.data.date.Time;

/**
 * @author Julian Schelker
 */
public class Log {

	private static Level logLevel;

	public static enum Level {
		ERROR(3), WARNING(2), INFO(1), VERBOSE(0);

		private int importance;

		private Level(int importance) {
			this.importance = importance;
		}
	}

	public static void setLevel(Level level) {
		logLevel = level;
	}

	public static void error(String msg) {
		log(msg, Level.ERROR);
	}

	public static void warning(String msg) {
		log(msg, Level.WARNING);
	}

	public static void info(String msg) {
		log(msg, Level.INFO);
	}

	public static void verbose(String msg) {
		log(msg, Level.VERBOSE);
	}

	private static void log(String msg, Level msgLogLevel) {
		if (logLevel.importance > msgLogLevel.importance)
			return;
		StackTraceElement stackElement = Thread.currentThread().getStackTrace()[3];

		Time currentTime = new Time();
		String fullClassName = stackElement.getClassName();
		String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

		String logMsg = currentTime.toString("H:i:s") + " " + logLevel.name() + " ["
			+ simpleClassName + " " + stackElement.getMethodName() + "] " + msg;
		System.out.println(logMsg);
	}

}
