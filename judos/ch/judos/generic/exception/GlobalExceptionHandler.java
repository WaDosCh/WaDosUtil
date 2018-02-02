package ch.judos.generic.exception;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URISyntaxException;

import ch.judos.generic.data.StringUtils;
import ch.judos.generic.data.date.Date;
import ch.judos.generic.data.date.Time;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.gui.Notification;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {

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
		catch (Exception e) {
			handle(e);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		String exceptionStackTrace = StringUtils.stringFromException(throwable);
		System.err.println(exceptionStackTrace); // log it for debugging

		// write to file
		File targetLogFolder = null;
		try {
			File f = new File(GlobalExceptionHandler.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath());
			targetLogFolder = f.getParentFile();
		}
		catch (URISyntaxException e) {
			// do nothing
		}
		if (targetLogFolder == null) {
			targetLogFolder = new File(".");
		}

		File file = new File(targetLogFolder, "Exception " + new Date() + " " + new Time()
			.toString("H\\hi") + ".txt");
		FileUtils.writeToFile(file, exceptionStackTrace);

		// show to user
		String shortened = StringUtils.takeMaxLinesFrom(exceptionStackTrace, 5);
		Notification.notifyErr("Error", "In this thread an error has occured: " + thread
			.getName() + "\n\n" + shortened + "\n\nA report has been saved in:\n " + file
				.getAbsolutePath());
	}

}
