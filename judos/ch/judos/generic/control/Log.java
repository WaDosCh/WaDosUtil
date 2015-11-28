package ch.judos.generic.control;

import ch.judos.generic.data.date.Time;

/**
 * @since 28.11.2015
 * @author Julian Schelker
 */
public class Log {

	private static Level logLevel;

	public static enum Level {
		ERROR(2), WARNING(1), INFO(0);

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

	private static void log(String msg, Level msgLogLevel) {
		if (logLevel.importance > msgLogLevel.importance)
			return;
		Time currentTime = new Time();
		String logMsg = currentTime.toString("H:i:s") + " " + logLevel.name() + " - " + msg;
		System.out.println(logMsg);
	}
}
