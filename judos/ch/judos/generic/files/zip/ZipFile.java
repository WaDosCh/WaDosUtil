package ch.judos.generic.files.zip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @since 26.02.2016
 * @author Julian Schelker
 */
public class ZipFile {

	private ArrayList<ZipFileFolder> folders;

	public ZipFile() {
		this.folders = new ArrayList<ZipFileFolder>();
	}

	public void addFolder(File folderName, String targetPathName) {
		this.folders.add(new ZipFileFolder(folderName, targetPathName));
	}

	public void saveZipAs(String zipFileName) throws IOException {
		Path p = Files.createFile(Paths.get(zipFileName));

		ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p));
		try {
			for (ZipFileFolder f : this.folders) {
				storeFolderInZip(f.folder, f.zipPathName, zs);
			}
		}
		finally {
			zs.close();
		}
	}

	private static void storeFolderInZip(File path, String name, ZipOutputStream zs)
		throws IOException {
		File[] files = path.listFiles((file) -> file.isFile());
		for (File file : files) {
			String originalName = file.getPath();
			String newName = originalName.replace(path.getPath(), name);
			newName = newName.replaceAll("\\\\", "/");

			zs.putNextEntry(new ZipEntry(newName));
			zs.write(Files.readAllBytes(Paths.get(file.getPath())));
			zs.closeEntry();
		}

		File[] folders = path.listFiles((file) -> file.isDirectory());
		for (File folder : folders) {
			storeFolderInZip(folder, name + "/" + folder.getName(), zs);
		}
	}
}
