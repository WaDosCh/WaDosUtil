package ch.judos.generic.files;

import java.io.File;

import ch.judos.generic.data.format.ByteData;

public class FileSize {

	public static String getFileSizeNice(String filename) {
		return getFileSizeNice(new File(filename));
	}

	public static String getFileSizeNice(File file) {
		long size = file.length();
		return getSizeNiceFromBytes(size);
	}

	public static String getSizeNiceFromBytes(long size) {
		return ByteData.autoFormat(size);
	}

}
