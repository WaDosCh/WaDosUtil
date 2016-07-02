package ch.judos.generic.files.zip;

import java.io.File;

/**
 * @since 26.02.2016
 * @author Julian Schelker
 */
public class ZipFileFolder {
	public File folder;
	public String zipPathName;

	public ZipFileFolder(File folderName, String targetPathName) {
		this.folder = folderName;
		this.zipPathName = targetPathName;
	}

}
