package ch.judos.generic.exception;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import ch.judos.generic.data.StringUtils;
import ch.judos.generic.data.date.Date;
import ch.judos.generic.data.date.Time;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.gui.Notification;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {

	public static File exceptionLogFolder = new File(".");

	public GlobalExceptionHandler() {
	}

	public static void handle(Throwable throwable) {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		Thread t = Thread.currentThread();
		handler.uncaughtException(t, throwable);
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
		String exceptionStackTrace = StringUtils.stringFromException(throwable);
		System.err.println(exceptionStackTrace); // log it for debugging

		File file = new File(exceptionLogFolder, "Exception " + new Date() + " " + new Time()
			.toString("H\\hi") + ".txt");
		FileUtils.writeToFile(file, exceptionStackTrace);

		// show to user
		String shortened = StringUtils.takeMaxLinesFrom(exceptionStackTrace, 5);
		Notification.notifyErr("Error", "In this thread an error has occured: " + thread
			.getName() + "\n\n" + shortened + "\n\nA report has been saved in:\n " + file
				.getAbsolutePath());
	}

}
