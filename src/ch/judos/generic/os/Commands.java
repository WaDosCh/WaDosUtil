package ch.judos.generic.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * @since 23.11.2015
 * @author Julian Schelker
 */
public class Commands {

	public static void runCommand(String command, Consumer<String> output,
		Consumer<String> error, boolean join) throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command);
		Thread t1 = startThreadForStream(process.getInputStream(), output, error);
		Thread t2 = startThreadForStream(process.getErrorStream(), error, error);
		if (join) {
			t1.join();
			t2.join();
		}
	}

	private static Thread startThreadForStream(InputStream inputStream,
		Consumer<String> output, Consumer<String> error) {
		Thread t = new Thread(() -> {
			try {
				forEachLine(inputStream, output);
			}
			catch (Exception e) {
				error.accept(e.getMessage());
			}
		});
		t.setDaemon(true);
		t.start();
		return t;
	}

	public static void forEachLine(InputStream in, Consumer<String> out) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			out.accept(line);
		}
	}
}
