package ch.judos.generic.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * 
 * 
 * @author m.thitithamasak
 */
public class thresholding {
	private static BufferedReader bReader;
	private static FileReader fReader;
	public static int height = 0;
	// private static FileOutputStream fWriter;
	public static double thresholdValue = 0.0;
	public static int width = 0;

	/**
	 * 
	 * This method close IO after reading
	 */
	public static void closeIO() {
		try {
			fReader.close();
			// pWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void print(int i) {
		System.out.println(i);
	}

	/**
	 * 
	 * This program takes no arguments in command line.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void print(String args) {
		System.out.println(args);
	}

	public static String thresholdOutBinaryImage(String fileName, double _thresholdValue,
		int _width, int _height) {
		thresholdValue = _thresholdValue;
		width = _width;
		height = _height;
		String outputFile = "BinaryImage.txt";
		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);
			String oneLine;
			double[][] binaryImage = new double[height][width];
			int lineCount = 0;
			while ((oneLine = bReader.readLine()) != null) {
				++lineCount;
				String[] pixelValue = new String[width];
				if (oneLine.length() != 0) {
					if (lineCount == 1) {
						String[] widthHeight = oneLine.trim().split(" ");
						height = Integer.parseInt(widthHeight[0]);
						width = Integer.parseInt(widthHeight[1]);
						// print("width : "+ width);
						// print("height : "+ height);
						// binaryImage = new double[height+1][width];
						binaryImage = new double[height + 1][width];
					}
					else {
						pixelValue = oneLine.split(" ");
						for (int i = 0; i < pixelValue.length; ++i) {
							if (Double.parseDouble(pixelValue[i]) < thresholdValue) {
								binaryImage[lineCount - 1][i] = 0;
							}
							else {
								binaryImage[lineCount - 1][i] = 1;
							}
						}
						// System.out.println("");
					}
				}
			}
			DistanceTransform.writeToFile(outputFile, binaryImage);
		}// end try block
		catch (IOException e) {
			System.out.println(e);
		}// end catch
		closeIO();
		return outputFile;
	}

	public static void
		thresholdOutToScreen(String fileName, double _thresholdValue, int _width) {
		thresholdValue = _thresholdValue;
		width = _width;
		// String outputFile="BinaryImage.txt";
		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);
			String oneLine;
			// double[][] binaryImage = new double[height][width];
			int lineCount = 0;
			while ((oneLine = bReader.readLine()) != null) {
				++lineCount;
				String[] pixelValue = new String[width];
				if (oneLine.length() != 0) {
					if (lineCount == 1) {
						String[] widthHeight = oneLine.trim().split(" ");
						height = Integer.parseInt(widthHeight[0]);
						width = Integer.parseInt(widthHeight[1]);
						// print("width : "+ width);
						// print("height : "+ height);
						// binaryImage = new double[height+1][width];
					}
					else {
						pixelValue = oneLine.split(" ");
						for (int i = 0; i < pixelValue.length; ++i) {
							if (Double.parseDouble(pixelValue[i]) < thresholdValue) {
								System.out.print("  ");
								// binaryImage[lineCount-1][i]= 0;
							}
							else {
								System.out.print("* ");
								// binaryImage[lineCount-1][i]= 1;
							}
						}
						System.out.println("");
					}
				}
			}
		}// end try block
		catch (IOException e) {
			System.out.println(e);
		}// end catch
		closeIO();
	}
}