package ch.judos.generic.exception;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StrSubstitutor;

import ch.judos.generic.data.StringUtils;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.gui.Notification;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {

	private static GlobalExceptionHandler instance;
	public static File exceptionLogFolder = new File(".");

	/**
	 * Take care not to use any characters that are not allowed in filenames!
	 * new SimpleDateFormat("yyyy-MM-dd hh'h'mm'm'ss.SSS");
	 */
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh'h'mm");

	/**
	 * customize the user's message that will be shown
	 */
	public static String message = "In this thread an error has occured: ${thread}\n\n"
		+ "${shortStackTrace}\n\nA report has been saved in:\n ${file}";

	public static Consumer<String> logOverride;

	public static GlobalExceptionHandler getInstance() {
		if (instance == null)
			instance = new GlobalExceptionHandler();
		return instance;
	}

	private GlobalExceptionHandler() {
	}

	public static void handle(Throwable throwable) {
		Thread t = Thread.currentThread();
		instance.uncaughtException(t, throwable);
	}

	public static void runAndHandle(RunnableThrowsException runnable) {
		try {
			runnable.run();
		}
		catch (Throwable e) {
			handle(e);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		String exceptionStackTrace = ExceptionUtils.getStackTrace(throwable);
		if (logOverride != null)
			logOverride.accept(exceptionStackTrace);
		else
			System.err.println(exceptionStackTrace); // log it for debugging

		File file = new File(exceptionLogFolder, "Exception " + dateFormat.format(new Date())
			+ ".txt");
		FileUtils.writeToFile(file, exceptionStackTrace);

		// show to user
		String shortened = StringUtils.takeMaxLinesFrom(exceptionStackTrace, 5);
		HashMap<String, String> values = new HashMap<>();
		values.put("thread", thread.getName());
		values.put("file", file.getAbsolutePath());
		values.put("shortStackTrace", shortened);

		String msg = new StrSubstitutor(values).replace(message);
		Notification.notifyErr("Error", msg);
	}

}
