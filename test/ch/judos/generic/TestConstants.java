package ch.judos.generic;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import ch.judos.generic.data.RandomJS;
import ch.judos.generic.files.FileSize;
import ch.judos.generic.files.FileUtils;

/**
 * @since 18.11.2014
 * @author Julian Schelker
 */
public class TestConstants {

	public static final String testFolderName = "bin/";

	private static File getDir() {
		File testFolder = new File(testFolderName);
		if (!testFolder.isDirectory())
			testFolder.mkdir();
		return testFolder;
	}

	public static File getTestData(long size) throws IOException {
		long remainingSize = size;
		File f = new File(getDir(), "testData-" + FileSize.getSizeNiceFromBytes(size)
			+ ".temp");
		try (Writer w = FileUtils.getWriterForFile(f)) {
			int bufSize = 1024 * 1024; // 1MB
			char[] data = null;
			while (remainingSize > 0) {
				if (remainingSize < bufSize)
					bufSize = (int) remainingSize;
				if (data == null || data.length != bufSize)
					data = new char[bufSize];
				w.write(data, 0, bufSize);
				remainingSize -= bufSize;
			}
		}
		return f;
	}

	public static File getTestFileForWriting() {
		return new File(getDir(), "testData-" + RandomJS.getInt(1000, 9999) + ".temp");
	}

}
