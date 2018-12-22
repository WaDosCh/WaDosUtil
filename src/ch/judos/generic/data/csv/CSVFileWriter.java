package ch.judos.generic.data.csv;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Use the constructor to create a CSVFileWriter object. You must specify all
 * the attributes you are using for your entries. Then use the addEntry method
 * to add entries. The order of the values must match the order of the
 * attributes.<br>
 * Use one of the write method to output the data to a file or an inputstream.
 * 
 * @since 04.01.2012
 * @author Julian Schelker
 * @version 1.0 / 04.01.2012
 */
public class CSVFileWriter {

	private String[] attributes;
	private ArrayList<String[]> entries;

	/**
	 * creates a csv writer with the given attributes
	 * 
	 * @param attributes
	 */
	public CSVFileWriter(String[] attributes) {
		this.attributes = attributes;
		this.entries = new ArrayList<>();
	}

	/**
	 * adds an entry to the csv file
	 * 
	 * @param entry
	 */
	public void addEntry(String[] entry) {
		if (entry.length != this.attributes.length)
			throw new InvalidParameterException(
				"Length of entry does not match expected number of attributes.");
		this.entries.add(entry);
	}

	/**
	 * writes the csv file to the target output
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void writeFile(File output) throws IOException {
		try (Writer w = new FileWriter(output)) {
			write(w);
		}
	}

	/**
	 * writes the csv file to the target output
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void writeStream(OutputStream output) throws IOException {
		write(new OutputStreamWriter(output));
	}

	/**
	 * writes the csv file to the target output
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void write(Writer output) throws IOException {
		BufferedWriter wr = new BufferedWriter(output);
		// If you get an error here you need to include
		// "Apache commons-lang3-3.1" into your project
		wr.write(String.join(CSVFile.separator, CSVFile.encodeForFile(this.attributes)));
		wr.write(CSVFile.linebreak);
		for (String[] entry : this.entries) {
			wr.write(String.join(CSVFile.separator, CSVFile.encodeForFile(entry)));
			wr.write(CSVFile.linebreak);
		}
		wr.flush();
	}

	/**
	 * writes the csv file to the target file defined by the pathname
	 * 
	 * @param pathname
	 * @throws IOException
	 */
	public void writeFile(String pathname) throws IOException {
		writeFile(new File(pathname));
	}

}
