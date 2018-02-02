package ch.judos.generic.control;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

/**
 * default level is INFO
 * 
 * @author Julian Schelker
 */
public class Log {

	public Level currentLogLevel = Level.INFO;

	/**
	 * For long format you could use <br>
	 * new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	 */
	public final DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.S");

	public boolean logToFile = false;
	public boolean logToConsole = true;

	public File logFile = new File("log.txt");

	/**
	 * anything logged, will also be logged in sublogger
	 */
	protected HashSet<Log> subLoggers;
	protected File outFile;
	protected OutputStreamWriter outStream;
	protected static Log instance;

	protected static Log getInstance() {
		if (instance == null) {
			instance = new Log();
			instance.logToFile = true;
		}
		return instance;
	}

	public Log() {
		this.subLoggers = new HashSet<Log>();
	}

	public static void verbose(String msg) {
		getInstance().logInternal(msg, Level.VERBOSE);
	}

	public static void verbose(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.VERBOSE);
	}

	public static void info(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.INFO);
	}

	public static void info(String msg) {
		getInstance().logInternal(msg, Level.INFO);
	}

	public static void warn(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.WARNING);
	}

	public static void warn(String msg) {
		getInstance().logInternal(msg, Level.WARNING);
	}

	public static void err(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.ERROR);
	}

	public static void err(String msg) {
		getInstance().logInternal(msg, Level.ERROR);
	}

	public static void log(String msg, Level msgLogLevel) {
		// just forward call to make sure correct stack trace element is useds
		getInstance().logInternal(msg, msgLogLevel);
	}

	public void logG(String msg, Level msgLogLevel) {
		// just forward call to make sure correct stack trace element is useds
		logInternal(msg, msgLogLevel);
	}

	private void logInternal(String msg, Level msgLogLevel) {
		if (this.currentLogLevel.importance > msgLogLevel.importance)
			return;
		StackTraceElement caller = Thread.currentThread().getStackTrace()[3];

		StringBuilder builder = new StringBuilder(150);
		builder.append(this.dateFormat.format(new Date())).append(" ");
		builder.append(this.currentLogLevel).append(" ");
		builder.append(" [").append(caller.getFileName()).append(":");
		builder.append(caller.getLineNumber()).append(" in " + caller.getMethodName() + "] ");
		builder.append(msg);
		logItActually(msg, msgLogLevel);
	}

	private void logItActually(String msg, Level msgLogLevel) {
		if (this.logToConsole) {
			if (msgLogLevel.importance >= Level.ERROR.importance)
				System.err.println(msg);
			else
				System.out.println(msg);
		}
		if (this.logToFile) {
			if (this.outFile == null || !this.outFile.equals(this.logFile)) {
				initializeOutstreamForLogging();
			}
			try {
				this.outStream.write(msg + System.lineSeparator());
				this.outStream.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initializeOutstreamForLogging() {
		this.outFile = this.logFile;
		try {
			this.outStream = new OutputStreamWriter(new FileOutputStream(this.logFile),
				StandardCharsets.UTF_8);
		}
		catch (FileNotFoundException e) {
			this.logToFile = false;
			err("Could not initialize OutputStream for logging to " + this.logFile.getName()
				+ ". LoggingToFile disabled. Error:", e);
		}
	}

	/**
	 * @param file
	 */
	public static void addSubLogger(Log logger) {
		getInstance().addSubLoggerI(logger);
	}
	public static void removeSubLogger(Log logger) {
		getInstance().removeSubLoggerI(logger);
	}

	public void addSubLoggerI(Log logger) {
		this.subLoggers.add(logger);
	}
	public void removeSubLoggerI(Log logger) {
		this.subLoggers.remove(logger);
	}

	public static enum Level {
		/** a immediate support email should be sent out */
		FATAL(4),
		/** this is a developer error */
		ERROR(3),
		/** abnormal use-case, might be ok to continue */
		WARNING(2),
		/** interesting information */
		INFO(1),
		/** what is currently going on */
		VERBOSE(0);

		int importance;

		private Level(int importance) {
			this.importance = importance;
		}

		public Level fromString(String str) {
			for (Level l : values()) {
				if (l.name().equals(str))
					return l;
			}
			return null;
		}
	}
}
