package ch.judos.generic.data.csv;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import ch.judos.generic.data.Map;
import ch.judos.generic.data.StringUtils;

/**
 * Use a static method to read data from an inputstream or a file. Then use the
 * getters to access the data from the csv-file.
 * 
 * @author Julian Schelker
 */
public class CSVFileReader {

	/**
	 * if it isn't muted, the readFile operations output some information about
	 * the csv file
	 */
	public static boolean muted = true;

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param input
	 *            the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader readFile(File input) throws IOException {
		return read(new FileReader(input));
	}

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param input
	 *            the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader readStream(InputStream input) throws IOException {
		return read(new InputStreamReader(input));
	}

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param input
	 *            the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader read(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		CSVFileReader result = new CSVFileReader(reader);
		return result;
	}

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param pathname
	 *            the filename of the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader readFile(String pathname) throws IOException {
		return readFile(new File(pathname));
	}

	/**
	 * @return an array of all existing attributes in this csv file
	 */
	public String[] getAttributes() {
		return this.attributes;
	}

	/**
	 * @return number of objects stored in the file
	 */
	public int countEntries() {
		return this.entities.length;
	}

	/**
	 * @param index
	 *            must be in the valid range (between 0 and countEntries()-1)
	 * @return a stored object of the file
	 * @throws IndexOutOfBoundsException
	 */
	public HashMap<String, String> getEntry(int index) throws IndexOutOfBoundsException {
		return this.entities[index];
	}

	protected String separation;
	private String[] attributes;
	private HashMap<String, String>[] entities;

	/**
	 * reads and parses a csv file
	 * 
	 * @param reader
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected CSVFileReader(BufferedReader reader) throws IOException {
		String line;
		String attributeLine = reader.readLine();
		HashMap<String, Integer> candidates = getSeparationCandidates(attributeLine);
		ArrayList<String> allLines = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			allLines.add(line);
		}
		removeSeparationCandidates(candidates, allLines);
		this.separation = getSeparationCharacter(candidates);

		this.attributes = CSVFile.decodeForValue(attributeLine.split(this.separation, -1));
		this.entities = (HashMap<String, String>[]) new HashMap[allLines.size()];
		int index = 0;
		for (String entity : allLines) {
			String[] values = CSVFile.decodeForValue(entity.split(this.separation, -1));
			this.entities[index] = new HashMap<>();
			for (int i = 0; i < this.attributes.length; i++) {
				this.entities[index].put(this.attributes[i], values[i]);
			}
			index++;
		}
		if (!muted) {
			System.out.println("Separation character = " + this.separation);
			System.out.println("Number of entities = " + allLines.size());
			System.out.println("Number of attributes = " + this.attributes.length);
		}
	}

	/**
	 * @param candidates
	 * @return
	 * @throws IOException
	 */
	protected String getSeparationCharacter(HashMap<String, Integer> candidates)
		throws IOException {
		for (String c : candidates.keySet())
			return c;
		throw new IOException("No separation candidates left, check file content.");
	}

	/**
	 * @param candidates
	 * @param lines
	 */
	protected void removeSeparationCandidates(HashMap<String, Integer> candidates,
		ArrayList<String> lines) {
		for (String entity : lines) {
			Iterator<Entry<String, Integer>> it = candidates.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Integer> en = it.next();
				int noc = StringUtils.countMatches(entity, en.getKey());
				if (noc != en.getValue()) {
					it.remove();
					if (candidates.size() == 1)
						return;
				}
			}
		}

	}

	/**
	 * finds all possible separation chars
	 * 
	 * @param lineWithAttributes
	 * @return
	 */
	protected HashMap<String, Integer> getSeparationCandidates(String lineWithAttributes) {
		HashMap<String, Integer> result = new HashMap<>();
		String s;
		for (char c : lineWithAttributes.toCharArray()) {
			if (Character.isAlphabetic(c) || Character.isDigit(c))
				continue;
			s = Character.toString(c);
			Map.add(result, s, 1);
		}
		return result;
	}

}
