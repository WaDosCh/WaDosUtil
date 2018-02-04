package ch.judos.generic.control;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StrSubstitutor;

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
	public DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.S");

	public boolean logToFile = false;
	public boolean logToConsole = true;

	/**
	 * the standard format of a log message.<br>
	 * Can be changed to an arbitrary different format by using the same
	 * placeholders that are defined here.<br>
	 * <br>
	 * <code> ${date} </code> The timestamp for the log message, uses dateFormat
	 * to format <br>
	 * <code> ${level} </code> Log level string, uses the enum name directly<br>
	 * <code> ${file} </code> In which file the log was sent<br>
	 * <code> ${line} </code> On which line the log was called<br>
	 * <code> ${method} </code> In which method the log happened<br>
	 * <code> ${msg} </code> The actual log message
	 */
	public String logFormat = "${date} ${level} in ${method} (${file}:${line}) ${msg}";

	public File logFile = new File("log.txt");

	/**
	 * Provide this to override the default behavior of the logger. It will
	 * receive a hashmap with all the placeholder values ({@link #logFormat} and
	 * the finally assembled message (with key 'message').
	 */
	public Consumer<Map<String, String>> loggingOverride;

	/**
	 * anything logged, will also be logged in sublogger
	 */
	protected HashSet<Log> subLoggers;
	protected File outFile;
	protected OutputStreamWriter outStream;
	protected static Log instance;

	public static Log getInstance() {
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

	public void verboseI(String msg) {
		logInternal(msg, Level.VERBOSE);
	}

	public static void verbose(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.VERBOSE);
	}

	public void verboseI(String msg, Object... args) {
		logInternal(msg + " " + StringUtils.join(args, ","), Level.VERBOSE);
	}

	public static void info(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.INFO);
	}

	public void infoI(String msg, Object... args) {
		logInternal(msg + " " + StringUtils.join(args, ","), Level.INFO);
	}

	public static void info(String msg) {
		getInstance().logInternal(msg, Level.INFO);
	}
	public void infoI(String msg) {
		logInternal(msg, Level.INFO);
	}

	public static void warn(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.WARNING);
	}
	public void warnI(String msg, Object... args) {
		logInternal(msg + " " + StringUtils.join(args, ","), Level.WARNING);
	}

	public static void warn(String msg) {
		getInstance().logInternal(msg, Level.WARNING);
	}
	public void warnI(String msg) {
		logInternal(msg, Level.WARNING);
	}
	public static void err(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.ERROR);
	}
	public void errI(String msg, Object... args) {
		logInternal(msg + " " + StringUtils.join(args, ","), Level.ERROR);
	}
	public static void err(String msg) {
		getInstance().logInternal(msg, Level.ERROR);
	}
	public void errI(String msg) {
		logInternal(msg, Level.ERROR);
	}

	public static void fatal(String msg, Object... args) {
		getInstance().logInternal(msg + " " + StringUtils.join(args, ","), Level.FATAL);
	}
	public void fatalI(String msg, Object... args) {
		logInternal(msg + " " + StringUtils.join(args, ","), Level.FATAL);
	}
	public static void fatal(String msg) {
		getInstance().logInternal(msg, Level.FATAL);
	}
	public void fatalI(String msg) {
		logInternal(msg, Level.FATAL);
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
		StackTraceElement caller = Thread.currentThread().getStackTrace()[3];

		HashMap<String, String> values = new HashMap<>(7);
		values.put("level", msgLogLevel.name());
		values.put("file", caller.getFileName());
		values.put("line", String.valueOf(caller.getLineNumber()));
		values.put("method", caller.getMethodName());
		values.put("msg", msg);

		Date now = new Date();
		logInternalWithValues(values, msgLogLevel, now);
	}

	private void logInternalWithValues(HashMap<String, String> values, Level msgLogLevel,
		Date now) {

		for (Log subLogger : this.subLoggers) {
			subLogger.logInternalWithValues(values, msgLogLevel, now);
		}

		if (this.currentLogLevel.importance > msgLogLevel.importance)
			return;

		values.put("date", this.dateFormat.format(now));
		String message = new StrSubstitutor(values).replace(this.logFormat);

		if (this.loggingOverride != null) {
			values.put("message", message);
			this.loggingOverride.accept(values);
		}
		else
			logInternalToTarget(message, msgLogLevel);
	}

	private void logInternalToTarget(String msg, Level msgLogLevel) {
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

	public void close() {
		if (this.outStream != null) {
			try {
				this.outStream.close();
			}
			catch (IOException e) {
				// fine
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
	}
}
