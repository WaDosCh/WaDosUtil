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

//TODO: do not overwrite exception file if existing. Add postfix number
public class GlobalExceptionHandler implements UncaughtExceptionHandler {

	public static boolean createExceptionFiles = true;

	public static File exceptionLogFolder = new File(".");

	/**
	 * Take care not to use any characters that are not allowed in filenames!
	 * new SimpleDateFormat("yyyy-MM-dd HH'h'mm'm'ss.SSS");
	 */
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH'h'mm");

	/**
	 * customize the user's message that will be shown
	 */
	public static String message = "In this thread an error has occured: ${thread}\n\n"
		+ "${shortStackTrace}";

	/**
	 * appended to the message if {@link #createExceptionFiles} is set to true
	 */
	public static String messageSaveToFile = "\n\nA report has been saved in:\\n ${file}";

	/**
	 * usually problems are directly logged to the System.err stream. If you set
	 * a consumer here you may override this behavior
	 */
	public static Consumer<String> logOverride;

	public static GlobalExceptionHandler getInstance() {
		if (instance == null)
			instance = new GlobalExceptionHandler();
		return instance;
	}

	private static GlobalExceptionHandler instance;

	private GlobalExceptionHandler() {
	}

	public static void handle(Throwable throwable) {
		Thread t = Thread.currentThread();
		getInstance().uncaughtException(t, throwable);
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

		HashMap<String, String> values = new HashMap<>();
		String shortened = StringUtils.takeMaxLinesFrom(exceptionStackTrace, 5);
		values.put("thread", thread.getName());
		values.put("shortStackTrace", shortened);

		// create exception file
		String messageAppended = "";
		if (createExceptionFiles) {
			File file = new File(exceptionLogFolder, "Exception " + dateFormat.format(
				new Date()) + ".txt");
			FileUtils.writeToFile(file, exceptionStackTrace);
			values.put("file", file.getAbsolutePath());
			messageAppended = new StrSubstitutor(values).replace(messageSaveToFile);
		}

		// show to user
		String msg = new StrSubstitutor(values).replace(message) + messageAppended;
		Notification.notifyErr("Error", msg);
	}

}
