package ch.judos.generic.files;

import java.awt.Component;
import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import ch.judos.generic.exception.ExceptionWithKey;

/**
 * @author Julian Schelker
 */
public class FileUtils extends File {

	private static final long serialVersionUID = -3775679979394334289L;

	public static void openFileWithDefaultApplication(File file) throws IOException {
		Desktop.getDesktop().open(file);
	}

	public static BufferedReader getReaderForFile(File f) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(f),
			CharsetDefault.CURRENT_CHARSET));
	}

	public static BufferedWriter getWriterForFile(File f) throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),
			CharsetDefault.CURRENT_CHARSET));
	}

	/**
	 * checks if the folder exists, if not it is created
	 * 
	 * @param file
	 */
	public static void checkOrCreateDir(File file) {
		if (!file.isDirectory())
			file.mkdir();
	}

	public static String getMd5HexForFile(File f) throws NoSuchAlgorithmException,
		IOException {
		try (FileInputStream is = new FileInputStream(f)) {
			return getMd5HexForStream(is);
		}
	}

	/**
	 * @param is
	 * @return the md5 hash code for this file
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 *             if the file could not be read
	 */
	public static String getMd5HexForStream(InputStream is) throws NoSuchAlgorithmException,
		IOException {
		byte[] md5 = getMd5ForFile(is);
		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < md5.length; i++) {
			hex.append(String.format("%02X", md5[i]));
		}
		return hex.toString();
	}

	public static byte[] getMd5ForFile(InputStream is) throws IOException,
		NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (DigestInputStream dis = new DigestInputStream(is, md)) {
			final int bufferSize = 8 * 1024;
			final byte[] buffer = new byte[bufferSize];
			for (;;) {
				int rsz = dis.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
			}
		}
		return md.digest();
	}

	/**
	 * @param sourceLocation
	 * @param targetLocation
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyDirectory(File sourceLocation, File targetLocation) {
		return copyDirectory(sourceLocation, targetLocation, null);
	}
	/**
	 * @param sourceLocation
	 * @param targetLocation
	 * @param exceptionList
	 *            occuring exceptions are stored in here
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyDirectory(File sourceLocation, File targetLocation,
		List<ExceptionWithKey> exceptionList) {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			boolean ok = true;
			if (sourceLocation.list() == null) {
				if (exceptionList != null) {
					exceptionList.add(new ExceptionWithKey("READ_DIRECTORY_FAILED",
						"Reading path which is not a directory: " + sourceLocation));
				}
				return false;
			}
			for (String childName : sourceLocation.list()) {
				ok &= copyDirectory(new File(sourceLocation, childName), new File(
					targetLocation, childName));
			}
			return ok;
		}
		return copyFile(sourceLocation, targetLocation, exceptionList);
	}

	/**
	 * @param f1
	 * @param f2
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyFile(File f1, File f2) {
		return copyFile(f1, f2, null);
	}

	public static boolean copyFile(File f1, File f2, List<ExceptionWithKey> exceptionList) {
		try {
			if (!f1.exists())
				return true;
			Files.copy(f1.toPath(), f2.toPath(), StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES);
			return true;
		}
		catch (Exception e) {
			if (exceptionList != null) {
				exceptionList.add(new ExceptionWithKey("COPY_FILE_FAILED", e.toString()));
			}
		}
		return false;
	}

	/**
	 * recursively deletes all files within this path, if it is a directory.
	 * otherwise just deletes the file
	 * 
	 * @param path
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean deleteDirectory(File path) {
		if (!path.exists())
			return true;
		if (path.isDirectory()) {
			for (File file : path.listFiles()) {
				if (file.isDirectory())
					deleteDirectory(file);
				else
					file.delete();
			}
		}
		return path.delete();
	}

	/**
	 * @return directory information of the desktop
	 */
	public static File getDesktopDir() {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		return filesys.getHomeDirectory();
	}

	/**
	 * @param file
	 * @return the filetype e.g. "jpg" "png" <br>
	 *         the empty string if it is not a file or doesn't have an extension
	 */
	public static String getExtension(File file) {
		String s = file.getName();
		int i = s.lastIndexOf('.');
		if (!file.isFile())
			return "";

		if (i >= 0 && i < s.length() - 1) {
			return s.substring(i + 1).toLowerCase();
		}
		return "";
	}

	/**
	 * @param file
	 * @return reads the whole content in the file and returns it as one
	 *         string.<br>
	 *         lines in the file are separated by \n
	 * @throws IOException
	 */
	public static String readAllFile(File file) throws IOException {
		try (BufferedReader b = getReaderForFile(file)) {
			String line;
			StringBuffer result = new StringBuffer();
			while ((line = b.readLine()) != null) {
				result.append(line + "\n");
			}
			return result.substring(0, result.length() - 1);
		}
	}

	/**
	 * @param file
	 * @return reads the whole content in the file and returns it as one
	 *         list.<br>
	 * @throws IOException
	 */
	public static ArrayList<String> readFileContent(File file) throws IOException {
		try (BufferedReader b = getReaderForFile(file)) {
			return readFileContent(b);
		}
	}

	public static ArrayList<String> readFileContent(BufferedReader reader) throws IOException {
		ArrayList<String> result = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null)
			result.add(line);

		return result;
	}

	/**
	 * @param parent
	 *            component of the popup
	 * @param title
	 *            displayed to the user
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(Component parent, String title) {
		return requestDir(parent, title, new File("."));
	}

	/**
	 * @param parent
	 *            component of the popup
	 * @param title
	 *            displayed to the user
	 * @param startAt
	 *            initial directory to come up with
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(Component parent, String title, File startAt) {
		JFileChooser chooser = new JFileChooser();
		try {
			chooser.setCurrentDirectory(startAt);
			chooser.setDialogTitle(title);

			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile();
			return null;
		}
		finally {
			chooser.setEnabled(false);
			chooser.setVisible(false);
		}
	}

	/**
	 * @param title
	 *            displayed to the user
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(String title) {
		return requestDir(null, title, new File("."));
	}

	/**
	 * @param title
	 *            displayed to the user
	 * @param startAt
	 *            initial directory to come up with
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(String title, File startAt) {
		return requestDir(null, title, startAt);
	}

	/**
	 * @return the file the user entered - null if cancel was pressed
	 */
	public static File requestFile() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		return null;
	}

	/**
	 * @param startPath
	 *            initial directory to come up with
	 * @param description
	 *            displayed to the user
	 * @param extensions
	 *            which are allowed
	 * @return the selected file or null is the operation is canceled
	 */
	public static File requestFile(File startPath, String description, String[] extensions) {
		JFileChooser chooser = new JFileChooser(startPath);

		String typeDesc = "";
		for (String t : extensions)
			typeDesc += t + " ";

		FileNameExtensionFilter filter = new FileNameExtensionFilter(typeDesc, extensions);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(description);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		return null;
	}

	/**
	 * @param description
	 *            some text to display to the user
	 * @param extensions
	 *            array of the extensions allowed
	 * @return the file the user entered - null if cancel was pressed
	 */
	public static File requestFile(String description, String extensions[]) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		return null;
	}

	/**
	 * @param startPath
	 *            initial directory to come up with
	 * @param description
	 *            displayed to the user
	 * @param extensions
	 *            which are allowed
	 * @return the selected file or null is the operation is canceled
	 */
	public static File saveFile(File startPath, String description, String extensions[]) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(description);
		chooser.setCurrentDirectory(startPath);
		String typeDesc = "";
		for (String t : extensions)
			typeDesc += t + " ";
		FileNameExtensionFilter filter = new FileNameExtensionFilter(typeDesc, extensions);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		// else
		return null;
	}

	/**
	 * @param file
	 * @param content
	 * @return true if writing succeeded
	 */
	public static boolean writeToFile(File file, String content) {
		try (BufferedWriter writer = getWriterForFile(file)) {
			writer.write(content);
			writer.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	public FileUtils(File f) {
		super(f.getAbsolutePath());
	}

	public String getEnding() {
		return splitName()[1];
	}

	public String getNameWithoutEnding() {
		return splitName()[0];
	}

	private String[] splitName() {
		String fileName = getName();
		Pattern pattern = Pattern.compile("(.+)\\.(\\w+)");
		Matcher m = pattern.matcher(fileName);
		if (m.matches()) {
			String name = m.group(1);
			String ending = m.group(2);
			return new String[]{name, ending};
		}
		throw new RuntimeException("Problem with splitting fileName: " + fileName);
	}

	public static int getFilesCount(File file, boolean directoryInclusive) {
		if (file.isFile())
			return 1;
		int count = (directoryInclusive ? 1 : 0);
		for (File f : file.listFiles())
			if (f.isDirectory())
				count += getFilesCount(f, directoryInclusive);
			else
				count++;
		return count;
	}

}
