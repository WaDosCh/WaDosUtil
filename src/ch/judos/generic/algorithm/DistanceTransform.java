package ch.judos.generic.algorithm;

import java.io.*;
import java.util.*;

/**
 * 
 * Author : Mongkol Thitithamasak Class : Image Processing Term : Fall 2011
 * Project : Distance Transform
 * 
 * Project Description : There are 2 types of input data files 1) binary image
 * files : ellipse.txt,Connected Circle.txt,diamond_connected.txt 2) grey scale
 * image files : donuts.txt,stpatricks leaf.txt
 * 
 * The program will first process the binary image files and then perform
 * distance transform
 * 
 * by traverse 2 passes over the binary image. The first pass it will scan from
 * left to right,
 * 
 * top to bottom by using 8 connected
 * 
 * neighbors and find minimum of the label and then assign the current position
 * with min+1
 * 
 * For the second pass, it will scan from right to left, bottom to top and find
 * minimum of neighbors
 * 
 * including itself then assign itself with min+1 if its value greater than
 * min+1.
 * 
 * It will result in each pixel will be labeled with distance from its location
 * to the edge.
 * 
 * 
 * Input: Grey scale Images and Binary Images with connected components
 * 
 * output: Enhanced Image with distance from the pixel to the border as label on
 * each pixel
 */
@SuppressWarnings("all")
public class DistanceTransform {
	public static ArrayList<Double> aboveThresholdAl;
	public static ArrayList<Integer> alPixelOri;
	public static ArrayList<Double> belowThresholdAl;
	private static BufferedReader bReader;
	private static BufferedWriter bWriter;
	public static int currentLabel = 0;
	public static ArrayList<ArrayList<Integer>> eqList;
	private static FileReader fReader;
	public static int height = 0;
	public static String[] histogramArray;
	public static Map<Double, Double> histogramArrayCountMap;
	public static ArrayList<Double> labelAl;
	public static ArrayList<Integer> neighborArray;
	public static double[][] ori2DImage = new double[1][1];
	public static Map<Integer, Integer> pixelCountMap;
	public static Map<Integer, ArrayList<pixel>> pixelMap;
	public static double pixelValue = 0.0;
	public static Map<Integer, ArrayList<Double>> propertiesMap;
	public static double thresholdValue = 0.0;
	public static int width = 0;

	/**
	 * 
	 * This method takes an ArrayList and create a histogram.
	 * 
	 * @param al
	 */
	public static void buildHistogram(ArrayList<Double> al) {
		histogramArray = new String[width * height];
		histogramArrayCountMap = new HashMap<>();
		initArray(histogramArray);
		for (int i = 0; i < al.size(); ++i) {
			pixelValue = al.get(i);
			histogramArray[(int) pixelValue] += "+";
			if (histogramArrayCountMap.get(pixelValue) == null) {
				histogramArrayCountMap.put(pixelValue, 1.0);
			}
			else {
				histogramArrayCountMap.put(pixelValue, histogramArrayCountMap.get(pixelValue)
					+ 1);
			}
		}
	}

	/**
	 * 
	 * This method calculates average from all values in the ArrayList
	 * 
	 * @param al
	 * 
	 * @return average of items in the list
	 */
	public static double calculateThresholdFromAvg(ArrayList<Double> al) {
		double sum = 0.0;
		// double avg = 0.0;
		if (al.isEmpty())
			return 0.0;
		for (int i = 0; i < al.size(); ++i) {
			sum += al.get(i);
		}
		return sum / al.size();
	}

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

	/**
	 * 
	 * This method convert from 2 dimensions double array to ArrayList
	 * 
	 * @param _Image
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<Double> convert2DimentoArrayList(double[][] _Image) {
		ArrayList<Double> al = new ArrayList<>();
		for (int i = 0; i < _Image.length; ++i) {
			for (int j = 0; j < _Image.length; ++j) {
				al.add(_Image[i][j]);
			}
		}
		return al;
	}

	/**
	 * 
	 * this method takes property map as an input and extract no of pixel based
	 * on given label.
	 * 
	 * Then, assign no of pixels as a label for each connected component.
	 * 
	 * @param image
	 * 
	 * @param map
	 * 
	 * @return image with no of pixels as label
	 */
	public static double[][] convertLabelToCount(double[][] image,
		Map<Integer, ArrayList<Double>> map) {
		Map<Integer, Integer> countMap = new HashMap<>();
		labelAl = new ArrayList<>();
		for (int i : map.keySet()) {
			countMap.put(i, map.get(i).get(7).intValue());
			labelAl.add(map.get(i).get(7));
		}
		for (int i = 0; i < image.length; ++i) {
			for (int j = 0; j < image[1].length; ++j) {
				if (image[i][j] > 0) {
					image[i][j] = countMap.get((int) image[i][j]);
				}
			}
		}
		return image;
	}

	/**
	 * 
	 * This method takes 2 dimension array and pad 2 rows(top and bottom) and
	 * 
	 * 2 columns (left and right) with the adjacent value
	 * 
	 * @param oriImage
	 * 
	 * @return 2 dimension enlarged image
	 */
	public static double[][] enlargeImage(double[][] oriImage) {
		int _width = 0;
		int _height = 0;
		_height = oriImage.length + 2;
		_width = oriImage[1].length + 2;
		double[][] enlargeImage = new double[_height][_width];
		for (int i = 0; i < oriImage.length; ++i) {
			for (int j = 0; j < oriImage[0].length; ++j) {
				double p = oriImage[i][j];
				enlargeImage[i + 1][j + 1] = p;
				if (i == 0) {
					if (i == 0 & (j == 0)) // top left
					{
						enlargeImage[i][j] = p;
						enlargeImage[i][j + 1] = p;
						enlargeImage[i + 1][j] = p;
					}
					else if (i == 0 & j == oriImage[1].length - 1) // top
					// right
					{
						enlargeImage[i][j + 1] = p;
						enlargeImage[i][j + 2] = p;
						enlargeImage[i + 1][j + 2] = p;
					}
					else {
						enlargeImage[i][j + 1] = p;
					}
				}
				else // i!=0
				{
					if (i != oriImage.length - 1 & j == 0) {
						enlargeImage[i + 1][j] = p;
					}
					if (i != oriImage.length - 1 & j == oriImage[1].length - 1) {
						enlargeImage[i + 1][j + 2] = p;
					}
					if (j == 0 & i == oriImage.length - 1)// bottom left
					{
						enlargeImage[i + 1][j] = p;
						enlargeImage[i + 2][j] = p;
						enlargeImage[i + 2][j + 1] = p;
					}
					else if (i == oriImage.length - 1 & j == oriImage[1].length - 1) // bottom
					// right
					{
						enlargeImage[i + 1][j + 2] = p;
						enlargeImage[i + 2][j + 2] = p;
						enlargeImage[i + 2][j + 1] = p;
					}
				}
			}
		}
		return enlargeImage;
	}

	/**
	 * 
	 * this method takes 2 number values and return the larger one.
	 * 
	 * @param a
	 * 
	 * @param b
	 * 
	 * @return
	 */
	public static int findLarger(int a, int b) {
		return a > b ? a : b;
	}

	/**
	 * 
	 * This method takes 2 number values and return the lesser one.
	 * 
	 * @param a
	 * 
	 * @param b
	 * 
	 * @return
	 */
	public static int findLesser(int a, int b) {
		return a < b ? a : b;
	}

	/**
	 * 
	 * This method traverse a neighbour list and return the minimum value.
	 * 
	 * @param neighbours
	 * 
	 * @return int minimum value
	 */
	public static int findMin(ArrayList<Integer> neighbours) {
		int min = 0;
		Collections.sort(neighbours);
		for (int i = 0; i < neighbours.size(); ++i) {
			if (neighbours.get(i) != 0) {
				min = neighbours.get(i);
				break;
			}
		}
		return min;
	}

	/**
	 * 
	 * This method traverse a neighbour list and return the minimum value.
	 * 
	 * @param neighbours
	 * 
	 * @return int minimum value
	 */
	public static int findMinDT(ArrayList<Integer> neighbours) {
		int min = 0;
		Collections.sort(neighbours);
		min = neighbours.get(0);
		return min;
	}

	/**
	 * 
	 * this method takes label number as an input and look up the same label
	 * with
	 * 
	 * less value.
	 * 
	 * @param label
	 * 
	 * @return equal min value
	 */
	public static int findMinEqual(double label) {
		int min = 0;
		for (int i = 0; i < eqList.size(); ++i) {
			if (eqList.get(i).get(0) == label) {
				Collections.sort(eqList.get(i));
				min = eqList.get(i).get(0);
				break;
			}
		}
		return min;
	}

	/**
	 * 
	 * a b c
	 * 
	 * d x
	 * 
	 * scan from left to right and top to bottom
	 * 
	 * for non-zeros values, if all neighbors are zeros, get a new label
	 * 
	 * if all non-zeros neighbors have the same label, take the label from a
	 * 
	 * neighbor.
	 * 
	 * if non-zeros neighbors have different labels, get the min label and
	 * 
	 * update equivalent table.
	 * 
	 * 
	 * 
	 * I implement equivalent table using adjacency list to simplify updating
	 * 
	 * of multiple labels. I keep track of labels that are equal in the same
	 * list.
	 * 
	 * To get min, I merely sort the list and pick the first item.
	 * 
	 * 
	 * 
	 * @param _image
	 * 
	 * @return double[][]image
	 */
	public static double[][] firstPass(double[][] _image) {
		eqList = new ArrayList<>();
		int nw, n, ne, w, c = 0;
		for (int i = 0; i < _image.length; ++i) {
			for (int j = 0; j < _image[0].length; ++j) {
				neighborArray = new ArrayList<>();
				try {
					nw = (int) _image[i - 1][j - 1];
					neighborArray.add(nw);
				}
				catch (Exception e) {
					nw = 0;
				}
				try {
					n = (int) _image[i - 1][j];
					neighborArray.add(n);
				}
				catch (Exception e) {
					n = 0;
				}
				try {
					ne = (int) _image[i - 1][j + 1];
					neighborArray.add(ne);
				}
				catch (Exception e) {
					ne = 0;
				}
				try {
					w = (int) _image[i][j - 1];
					neighborArray.add(w);
				}
				catch (Exception e) {
					w = 0;
				}
				c = (int) _image[i][j];
				if (c > 0) {
					if (nw == 0 && n == 0 && ne == 0 && w == 0)// case 1 : all
					// are zeros
					{
						ArrayList<Integer> al = new ArrayList<>();
						al.add(++currentLabel);
						eqList.add(al);
						_image[i][j] = currentLabel;
						// print2DimensionArray(_image);
						// print("");
					}
					else if (!neighborArray.isEmpty() && nonZeroAreTheSameLabel(neighborArray))// case
																								// 2
																								// :
																								// all
					// the same
					// values
					{
						_image[i][j] = findMin(neighborArray); // take value
						// from one of
						// the neighbors
						// print2DimensionArray(_image);
						// print("");
					}
					else if (!neighborArray.isEmpty() && !nonZeroAreTheSameLabel(
						neighborArray)) // case
										// 3
										// :
					// not all
					// are the
					// same
					{
						_image[i][j] = findMin(neighborArray);
						// print2DimensionArray(_image);
						// print("");
					}
				}
			}
		}
		return _image;
	}

	/**
	 * 
	 * a b c
	 * 
	 * d x
	 * 
	 * scan from left to right and top to bottom
	 * 
	 * for non-zeros values, if all neighbors are zeros, get a new label
	 * 
	 * if all non-zeros neighbors have the same label, take the label from a
	 * 
	 * neighbor.
	 * 
	 * if non-zeros neighbors have different labels, get the min label and
	 * 
	 * update equivalent table.
	 * 
	 * 
	 * 
	 * I implement equivalent table using adjacency list to simplify updating
	 * 
	 * of multiple labels. I keep track of labels that are equal in the same
	 * list.
	 * 
	 * To get min, I merely sort the list and pick the first item.
	 * 
	 * 
	 * 
	 * @param _image
	 * 
	 * @return double[][]image
	 */
	public static double[][] firstPassDT(double[][] _image) {
		eqList = new ArrayList<>();
		int nw, n, ne, w, x = 0;
		for (int i = 0; i < _image.length; ++i) {
			for (int j = 0; j < _image[0].length; ++j) {
				neighborArray = new ArrayList<>();
				try {
					nw = (int) _image[i - 1][j - 1];
					neighborArray.add(nw);
				}
				catch (Exception e) {
					nw = 0;
					neighborArray.add(nw);
				}
				try {
					n = (int) _image[i - 1][j];
					neighborArray.add(n);
				}
				catch (Exception e) {
					n = 0;
					neighborArray.add(n);
				}
				try {
					ne = (int) _image[i - 1][j + 1];
					neighborArray.add(ne);
				}
				catch (Exception e) {
					ne = 0;
					neighborArray.add(ne);
				}
				try {
					w = (int) _image[i][j - 1];
					neighborArray.add(w);
				}
				catch (Exception e) {
					w = 0;
					neighborArray.add(w);
				}
				x = (int) _image[i][j];
				if (x > 0) {
					_image[i][j] = findMinDT(neighborArray) + 1;
				}
			}
		}
		return _image;
	}

	/**
	 * 
	 * This method takes ArrayList of pixel values, threshold values and split
	 * 
	 * pixels into above threshold and below threshold.
	 * 
	 * @param al
	 * 
	 * @param _tresholdValue
	 */
	public static void getAboveAndBelowList(ArrayList<Double> al, double _tresholdValue) {
		aboveThresholdAl = new ArrayList<>();
		belowThresholdAl = new ArrayList<>();
		for (int i = 0; i < al.size(); ++i) {
			if (al.get(i) > _tresholdValue) {
				aboveThresholdAl.add(al.get(i));
			}
			else {
				belowThresholdAl.add(al.get(i));
			}
		}
	}

	/**
	 * 
	 * This method takes the property map as an input and calculate the second
	 * 
	 * moment properties and then return the added property map.
	 * 
	 * @param map
	 * 
	 * @param image
	 * 
	 * @return added property map Map&lt;Integer, ArrayList&lt;Double&gt;&gt;
	 */
	public static Map<Integer, ArrayList<Double>> getAdditional5Properties(
		Map<Integer, ArrayList<Double>> map, double[][] image) {
		for (int i : map.keySet()) {
			int minRow = map.get(i).get(1).intValue();
			int maxRow = map.get(i).get(2).intValue();
			int minCol = map.get(i).get(3).intValue();
			int maxCol = map.get(i).get(4).intValue();
			int noOfPixel = map.get(i).get(7).intValue();
			double cr = 0.0;
			double cc = 0.0;
			double rc = 0.0;
			double r2 = 0.0;
			double c2 = 0.0;
			double Vr = 0.0;
			double Vc = 0.0;
			double sumR = 0.0;
			double sumC = 0.0;
			double sumRC = 0.0;
			double sumR2 = 0.0;
			double sumC2 = 0.0;
			double sumVr = 0.0;
			double sumVc = 0.0;
			for (int r = minRow; r < maxRow + 1; ++r) {
				for (int c = minCol; c < maxCol + 1; ++c) {
					if (image[r][c] > 0) {
						sumR += r;
						sumC += c;
						sumRC += r * c;
						sumR2 += Math.pow(r, 2);
						sumC2 += Math.pow(c, 2);
					}
				}
			}
			cr = sumR / noOfPixel;
			cc = sumC / noOfPixel;
			rc = sumRC / noOfPixel;
			r2 = sumR2 / noOfPixel;
			c2 = sumC2 / noOfPixel;
			map.get(i).add(cr);
			map.get(i).add(cc);
			map.get(i).add(rc);
			map.get(i).add(r2);
			map.get(i).add(c2);
			for (int r = minRow; r < maxRow + 1; ++r) {
				for (int c = minCol; c < maxCol + 1; ++c) {
					if (image[r][c] > 0) {
						sumVr += Math.pow((r - cr), 2);
						sumVc += Math.pow((c - cc), 2);
					}
				}
			}
			Vr = sumVr / noOfPixel;
			Vc = sumVc / noOfPixel;
			map.get(i).add(Vr);
			map.get(i).add(Vc);
		}
		return map;
	}

	/**
	 * 
	 * This method takes ArrayList and No of Items and get the median
	 * 
	 * @param al
	 * 
	 * @param NoofItems
	 * 
	 * @return median value
	 */
	public static double getMedian(ArrayList<Double> al, int NoofItems) {
		return al.get((NoofItems + 1) / 2);
	}

	public static ArrayList<Double> getTopFromList(int top, ArrayList<Double> al) {
		ArrayList<Double> tempAl = new ArrayList<>();
		for (int i = 0; i < al.size(); ++i) {
			if (i < top) {
				tempAl.add(al.get(i));
			}
		}
		return tempAl;
	}

	public static String[] initArray(String[] stringArray) {
		for (int i = 0; i < stringArray.length; ++i) {
			stringArray[i] = "";
		}
		return stringArray;
	}

	/**
	 * 
	 * This method traverse HashMap pixel counts and return top candidates
	 * 
	 * @param NoOfTop
	 * 
	 * @param histogramArrayCountMap
	 * 
	 * @return ArrayList of top candidates
	 */
	public static ArrayList<Double> listTopCandidate(int NoOfTop,
		Map<Double, Double> histogramArrayCountMap) {
		ArrayList<Double> valueList = new ArrayList<>();
		valueList.addAll(histogramArrayCountMap.values());
		Collections.sort(valueList, Collections.reverseOrder());
		ArrayList<Double> candidateList = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < valueList.size(); ++i) {
			for (double s : histogramArrayCountMap.keySet()) {
				if (histogramArrayCountMap.get(s) == valueList.get(i)) {
					candidateList.add(histogramArrayCountMap.get(s));
					++count;
				}
			}
			if (NoOfTop == count) {
				break;
			}
		}
		return candidateList;
	}

	/**
	 * 
	 * This program takes no arguments in command line.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		double aboveAvg = 0.0;
		double belowAvg = 0.0;
		double newAvg = 0.0;
		String fileName = "";
		// try
		// {
		String[] inputFile = {"ellipse.txt", "Connected Circle.txt", "diamond_connected.txt"};
		for (int i = 0; i < inputFile.length; ++i) {
			fileName = inputFile[i];
			String BinaryImage = fileName;
			print("Input Data File :" + fileName);
			// read binary image
			readFromFile(BinaryImage);
			print("");
			print("Binary Image: ");
			print("");
			print2DimensionArrayInt(ori2DImage);
			double[][] firstPassImage = new double[height][width];
			firstPassImage = firstPassDT(ori2DImage);
			print("");
			print("First Pass Image: ");
			print("");
			print2DimensionArrayPretty(firstPassImage);
			double[][] secondPassImage = new double[height][width];
			secondPassImage = secondPassDT(firstPassImage);
			print("Second Pass Image: ");
			print("");
			print("");
			print2DimensionArrayPretty(secondPassImage);
		}
		String[] inputFile1 = {"donuts.txt", "stpatricks leaf.txt"};
		for (int i = 0; i < inputFile1.length; ++i) {
			fileName = inputFile1[i];
			readFromFile(fileName);
			print("Input Data File :" + fileName);
			print("");
			print("GreyScale Image: ");
			print("");
			print2DimensionArrayInt(ori2DImage);
			double[][] enlargeImage = new double[height + 2][width + 2];
			enlargeImage = enlargeImage(ori2DImage);
			print("");
			print("Median Filtering Operation - Enhanced Image : ");
			print("");
			// an enhance image is created with Median Filtering Operaion
			double[][] image = new double[height][width];
			image = ImageFilteringOperation.medianFilteringOperation(enlargeImage, image, 3);
			// print2DimensionArray(image);
			print2DimensionArrayInt(image);
			ArrayList<Double> al = new ArrayList<>();
			al = convert2DimentoArrayList(image);
			// build histogram from an enhance image
			buildHistogram(al);
			print("");
			print("Histogram : ");
			printHistogram(histogramArray, (int) ImageFilteringOperation.max);
			// calculate threshold values
			ArrayList<Double> possibleThresholdAl = new ArrayList<>();
			possibleThresholdAl = listTopCandidate(11, histogramArrayCountMap);
			print("");
			print("Top 11 highest values : ");
			printArrayList(possibleThresholdAl);
			thresholdValue = calculateThresholdFromAvg(possibleThresholdAl);
			thresholdValue = getMedian(possibleThresholdAl, 11);
			int count = 0;
			print("");
			print("Calculating Threshold values : ");
			while (thresholdValue != newAvg) {
				if (count != 0) {
					thresholdValue = newAvg;
					if (count == 100)
						break;
				}
				getAboveAndBelowList(al, thresholdValue);
				aboveAvg = calculateThresholdFromAvg(aboveThresholdAl);
				belowAvg = calculateThresholdFromAvg(belowThresholdAl);
				newAvg = (aboveAvg + belowAvg) / 2;
				print("start threshold value : " + thresholdValue);
				print("new threshold value : " + String.valueOf(newAvg));
				++count;
			}
			// create a binary image and print pretty binary to screen
			fileName = "Enhance.txt";
			File myFile = new File(fileName);
			myFile.delete();
			writeToFile(fileName, image);
			fileName = Thresholding.thresholdOutBinaryImage(fileName, thresholdValue, width,
				height);
			String BinaryImage = fileName;
			// read binary image
			readFromFile(BinaryImage);
			print("");
			print("Binary Image: ");
			print("");
			print2DimensionArrayInt(ori2DImage);
			double[][] firstPassImage = new double[height][width];
			firstPassImage = firstPassDT(ori2DImage);
			print("");
			print("First Pass Image: ");
			print("");
			print2DimensionArrayPretty(firstPassImage);
			double[][] secondPassImage = new double[height][width];
			secondPassImage = secondPassDT(firstPassImage);
			print("Second Pass Image: ");
			print("");
			print("");
			print2DimensionArrayPretty(secondPassImage);
		}
	}

	/**
	 * 
	 * This method checks whether neighbors have the same label.
	 * 
	 * @param al
	 * 
	 * @return true if have the same values otherwise returns false.
	 */
	public static boolean nonZeroAreTheSameLabel(ArrayList<Integer> al) {
		boolean allTheSame = true;
		Collections.sort(al); // sort ascending
		for (int i = 0; i < al.size(); ++i) {
			if (i < al.size() - 1 && al.get(i) != 0) {
				if (al.get(i) != al.get(i + 1)) {
					allTheSame = false;
					break;
				}
			}
		}
		return allTheSame;
	}

	public static void print(int i) {
		System.out.println(i);
	}

	public static void print(String s) {
		System.out.println(s);
	}

	/**
	 * 
	 * This method print 2 dimension array to screen
	 * 
	 * @param twodimenstionArray
	 */
	public static void print2DimensionArray(double[][] twodimenstionArray) {
		for (int i = 0; i < twodimenstionArray.length; ++i) {
			for (int j = 0; j < twodimenstionArray[0].length; ++j) {
				System.out.print(String.valueOf(roundToDecimals(twodimenstionArray[i][j], 2)
					+ " "));
			}
			print("");
		}
	}

	/**
	 * 
	 * This method print 2 dimension array to screen
	 * 
	 * @param twodimenstionArray
	 */
	public static void print2DimensionArrayInt(double[][] twodimenstionArray) {
		for (int i = 0; i < twodimenstionArray.length; ++i) {
			for (int j = 0; j < twodimenstionArray[0].length; ++j) {
				System.out.print((int) twodimenstionArray[i][j] + " ");
			}
			print("");
		}
	}

	public static void print2DimensionArrayPretty(double[][] twodimenstionArray) {
		for (int i = 0; i < twodimenstionArray.length; ++i) {
			for (int j = 0; j < twodimenstionArray[0].length; ++j) {
				if (twodimenstionArray[i][j] != 0) {
					System.out.print(String.valueOf((int) twodimenstionArray[i][j]) + " ");
				}
				else {
					System.out.print("  ");
				}
			}
			print("");
		}
	}

	public static void printArrayList(ArrayList<Double> al) {
		for (int i = 0; i < al.size(); ++i) {
			print(String.valueOf(al.get(i)));
		}
	}

	public static void printArrayListInt(ArrayList<Integer> al) {
		for (int i = 0; i < al.size(); ++i) {
			print(String.valueOf(al.get(i)));
		}
	}

	public static void printHistogram(String[] al, int max) {
		int count = 0;
		for (int i = 0; i < al.length; ++i) {
			if (count < max) {
				print(i + " :" + histogramArray[i]);
			}
			else {
				break;
			}
			++count;
		}
	}

	public static void printMap(Map<Integer, ArrayList<Double>> map) {
		print(
			"label     minRow    maxRow      minCol    maxCol       width        height      noOfPixel         CR         CC         RC            R2           C2            Vr        Vc     ");
		for (int i : map.keySet()) {
			ArrayList<Double> al = new ArrayList<>();
			al = map.get(i);
			// System.out.print(i + " : ") ;
			printPropertyArrayList(al);
		}
	}

	public static void printPropertyArrayList(ArrayList<Double> al) {
		int count = 0;
		for (int i = 0; i < al.size(); ++i) {
			if (count < 8) {
				System.out.print(al.get(i).intValue() + "   ");
			}
			else {
				System.out.print(String.valueOf(roundToDecimals(al.get(i), 1)) + "   ");
			}
			if (i < al.size() - 1) {
				System.out.print("      ");
			}
			++count;
		}
		print("");
	}

	/**
	 * 
	 * This method read a file one line at a time.
	 * 
	 * Split data on each line with " ".
	 * 
	 * It generate one arrayList of pixels and double[][] of pixels.
	 * 
	 * Continue reading until the end of the file.
	 * 
	 * @param fileName
	 */
	public static void readFromFile(String fileName) {
		alPixelOri = new ArrayList<>();
		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);
			String oneLine;
			int lineCount = 0;
			while ((oneLine = bReader.readLine()) != null) {
				++lineCount;
				if (lineCount == 1) {
					String[] widthHeight = oneLine.trim().split(" ");
					height = Integer.parseInt(widthHeight[0]);
					width = Integer.parseInt(widthHeight[1]);
					// print("width : "+ width);
					// print("height : "+ height);
					ori2DImage = new double[height][width];
				}
				else {
					// ori2DImage = new double[width][height];
					String[] pixelValueArray = new String[width];
					if (oneLine.length() != 0) {
						pixelValueArray = oneLine.split(" ");
						for (int i = 0; i < pixelValueArray.length; ++i) {
							if (pixelValueArray[i].trim().length() != 0) {
								alPixelOri.add(Integer.parseInt(pixelValueArray[i]));
							}
						}
					}
				}
			}
			lineCount = 0;
			for (int i = 0; i < alPixelOri.size(); ++i) {
				if (i % width == 0) {
					++lineCount;
				}
				ori2DImage[lineCount - 1][i % width] = alPixelOri.get(i);
			}
		} // end try block
		catch (IOException e) {
			System.out.println(e);
		} // end catch
		closeIO();
	}

	/**
	 * 
	 * This method read a file one line at a time.
	 * 
	 * Split data on each line with " ".
	 * 
	 * It generate one arrayList of pixels and double[][] of pixels.
	 * 
	 * Continue reading until the end of the file.
	 * 
	 * @param fileName
	 */
	public static void readFromFileEachInt(String fileName) {
		alPixelOri = new ArrayList<>();
		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);
			// String oneLine;
			Scanner scan;
			int intCount = 0;
			while (bReader.readLine() != null) {
				scan = new Scanner(System.in);
				++intCount;
				if (intCount == 1) {
					height = scan.nextInt();
				}
				else if (intCount == 2) {
					width = scan.nextInt();
				}
				else {
					int pixel = scan.nextInt();
					alPixelOri.add(pixel);
					// String[]widthHeight = oneLine.trim().split(" ");
					// height = Integer.parseInt(widthHeight[0]);
					// width = Integer.parseInt(widthHeight[1]);
					// print("width : "+ width);
					// print("height : "+ height);
				}
				// //
			}
			printArrayListInt(alPixelOri);
			// lineCount = 0;
			// ori2DImage = new double[height][width];
			// for (int i = 0; i < alPixelOri.size(); ++i)
			// {
			// if (i%width == 0)
			// {
			// ++lineCount;
			// }
			// ori2DImage[lineCount-1][i%width] = alPixelOri.get(i);
			// }
		} // end try block
		catch (IOException e) {
			System.out.println(e);
		} // end catch
		closeIO();
	}

	/**
	 * 
	 * round double to 2 decimals
	 * 
	 * @param d
	 * 
	 * @param c
	 * 
	 * @return double value with 2 decimals
	 */
	public static double roundToDecimals(double d, int c) {
		int temp = (int) ((d * Math.pow(10, c)));
		return ((temp) / Math.pow(10, c));
	}

	/**
	 * 
	 * x a
	 * 
	 * b c d
	 * 
	 * 
	 * 
	 * This method scans from right to left and bottom to top.
	 * 
	 * only when pixel x is not zero, if all neighbors are zeros, do nothing
	 * 
	 * if all non-zero neighbors have the same label, get label from a neighbor
	 * 
	 * if neighbors have different labels, get min from neighbors and x
	 * 
	 * and also look up equivalent table to get the same label with less value.
	 * 
	 * 
	 * 
	 * @param _image
	 * 
	 * @return double[][] image
	 */
	public static double[][] secondPass(double[][] _image) {
		eqList = new ArrayList<>();
		int ea, sw, s, se, c = 0;
		for (int i = _image.length - 1; i > 0; --i) {
			for (int j = _image[0].length - 1; j > 0; --j) {
				neighborArray = new ArrayList<>();
				try {
					ea = (int) _image[i][j + 1];
					neighborArray.add(ea);
				}
				catch (Exception e) {
					ea = 0;
				}
				try {
					sw = (int) _image[i + 1][j - 1];
					neighborArray.add(sw);
				}
				catch (Exception e) {
					sw = 0;
				}
				try {
					s = (int) _image[i + 1][j];
					neighborArray.add(s);
				}
				catch (Exception e) {
					s = 0;
				}
				try {
					se = (int) _image[i + 1][j + 1];
					neighborArray.add(se);
				}
				catch (Exception e) {
					se = 0;
				}
				c = (int) _image[i][j];
				if (c > 0) {
					if (ea == 0 && sw == 0 && s == 0 && se == 0)// case 1 : all
					// are zeros
					{
						// do nothing
					}
					else if (!neighborArray.isEmpty() && nonZeroAreTheSameLabel(neighborArray))// case
																								// 2
																								// :
																								// all
					// the same
					// values
					{
						_image[i][j] = findMin(neighborArray); // take value
						// from one of
						// the neighbors
						// print2DimensionArray(_image);
						// print("");
					}
					else if (!neighborArray.isEmpty() && !nonZeroAreTheSameLabel(
						neighborArray)) // case
										// 3
										// :
					// not all
					// are the
					// same
					{
						neighborArray.add(c);
						_image[i][j] = findMinEqual(findMin(neighborArray));
						// print2DimensionArray(_image);
						// print("");
					}
				}
			}
		}
		return _image;
	}

	/**
	 * 
	 * x a
	 * 
	 * b c d
	 * 
	 * 
	 * 
	 * This method scans from right to left and bottom to top.
	 * 
	 * only when pixel x is not zero, if all neighbors are zeros, do nothing
	 * 
	 * if all non-zero neighbors have the same label, get label from a neighbor
	 * 
	 * if neighbors have different labels, get min from neighbors and x
	 * 
	 * and also look up equivalent table to get the same label with less value.
	 * 
	 * 
	 * 
	 * @param _image
	 * 
	 * @return double[][] image
	 */
	public static double[][] secondPassDT(double[][] _image) {
		eqList = new ArrayList<>();
		int ea, sw, s, se, x = 0;
		for (int i = _image.length - 1; i > 0; --i) {
			for (int j = _image[0].length - 1; j > 0; --j) {
				neighborArray = new ArrayList<>();
				try {
					ea = (int) _image[i][j + 1];
					neighborArray.add(ea);
				}
				catch (Exception e) {
					ea = 0;
					neighborArray.add(ea);
				}
				try {
					sw = (int) _image[i + 1][j - 1];
					neighborArray.add(sw);
				}
				catch (Exception e) {
					sw = 0;
					neighborArray.add(sw);
				}
				try {
					s = (int) _image[i + 1][j];
					neighborArray.add(s);
				}
				catch (Exception e) {
					s = 0;
					neighborArray.add(s);
				}
				try {
					se = (int) _image[i + 1][j + 1];
					neighborArray.add(se);
				}
				catch (Exception e) {
					se = 0;
					neighborArray.add(se);
				}
				x = (int) _image[i][j];
				neighborArray.add(x);
				if (x > 0) {
					int minp1 = findMinDT(neighborArray) + 1;
					if (x > minp1) {
						_image[i][j] = minp1;
					}
				}
			}
		}
		return _image;
	}

	/**
	 * 
	 * This method scan the input image and fetch each label along with its
	 * location
	 * 
	 * to pixel map.
	 * 
	 * Then, for each label(connected component) set find properties of each CC.
	 * 
	 * return property map as output
	 * 
	 * @param image
	 *            the image
	 * 
	 * @return property map Map&lt;Integer,ArrayList&lt;Double&gt;&gt;
	 */
	public static Map<Integer, ArrayList<Double>> thirdPass(double[][] image) {
		int label = 0;
		int noOfPixel = 0;
		int minRow = 0;
		int minCol = 0;
		int maxRow = 0;
		int maxCol = 0;
		int cc_width = 0;
		int cc_height = 0;
		boolean first = true;
		pixelCountMap = new HashMap<>();
		pixelMap = new HashMap<>();
		ArrayList<pixel> pixelArrayList;
		propertiesMap = new HashMap<>();
		for (int i = 0; i < image.length; ++i) {
			for (int j = 0; j < image[1].length; ++j) {
				label = (int) image[i][j];
				// put all pixel in pixelMap
				if (label > 0) {
					// if the label return null count then it is a new label,
					// put 1
					// if the label return number, then it is old lable, add 1
					pixelCountMap.put(label, pixelCountMap.get(label) == null ? 1
						: pixelCountMap.get(label) + 1);
					if (!pixelMap.containsKey(label)) // does not contain this
					// label
					{
						pixelArrayList = new ArrayList<>();
						pixelMap.put(label, pixelArrayList);
					}
					pixel p = new pixel(i, j); // offset height row
					pixelMap.get(label).add(p);
				}
			}
		}
		// each label list, do operation
		for (int l : pixelMap.keySet()) {
			ArrayList<pixel> al = new ArrayList<>();
			al = pixelMap.get(l);
			first = true;
			for (int i = 0; i < al.size(); ++i) {
				pixel p = new pixel();
				p = al.get(i);
				if (first) {
					minRow = p.row;
					maxRow = p.row;
					minCol = p.col;
					maxCol = p.col;
					first = false;
				}
				else {
					minRow = findLesser(p.row, minRow);
					maxRow = findLarger(p.row, maxRow);
					minCol = findLesser(p.row, minCol);
					maxCol = findLarger(p.row, maxCol);
				}
			}
			labelAl = new ArrayList<>();
			labelAl.add((double) l);
			labelAl.add((double) minRow);
			labelAl.add((double) maxRow);
			labelAl.add((double) minCol);
			labelAl.add((double) maxCol);
			cc_width = maxCol - minCol;
			cc_height = maxRow - minRow;
			labelAl.add((double) cc_width);
			labelAl.add((double) cc_height);
			noOfPixel = pixelCountMap.get(l);
			labelAl.add((double) noOfPixel);
			propertiesMap.put(l, labelAl);
			minRow = 0;
			maxRow = 0;
			minCol = 0;
			maxCol = 0;
			first = true;
		}
		return propertiesMap;
	}

	public static String thresholdOutBinaryImage(String fileName, double _thresholdValue,
		int _width) {
		thresholdValue = _thresholdValue;
		width = _width;
		String outputFile = "BinaryImage.txt";
		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);
			String oneLine;
			double[][] binaryImage = new double[height][width];
			int lineCount = 0;
			while ((oneLine = bReader.readLine()) != null) {
				++lineCount;
				String[] pixelValueX = new String[width];
				if (oneLine.length() != 0) {
					if (lineCount == 1) {
						String[] widthHeight = oneLine.trim().split(" ");
						height = Integer.parseInt(widthHeight[0]);
						width = Integer.parseInt(widthHeight[1]);
						print("width : " + width);
						print("height : " + height);
						binaryImage = new double[height][width];
					}
					else {
						pixelValueX = oneLine.split(" ");
						for (int i = 0; i < pixelValueX.length; ++i) {
							if (Double.parseDouble(pixelValueX[i]) < thresholdValue) {
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
		} // end try block
		catch (IOException e) {
			System.out.println(e);
		} // end catch
		closeIO();
		return outputFile;
	}

	public static void writeToFile(String fileName, double[][] image) {
		try {
			File myFile = new File(fileName);
			myFile.delete();
			bWriter = new BufferedWriter(new FileWriter(fileName, true));
			for (int i = 0; i < image.length; ++i) {
				String oneLine = "";
				for (int j = 0; j < image[1].length; ++j) {
					if (i == 0 && j == 0) {
						oneLine = image.length + " " + (image[1].length);
						bWriter.write(oneLine);
						bWriter.newLine();
						bWriter.flush();
						oneLine = "";
					}
					oneLine += (int) image[i][j] + " ";
				}
				bWriter.write(oneLine);
				bWriter.newLine();
				bWriter.flush();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(String fileName, Map<Integer, ArrayList<Integer>> map) {
		try {
			File myFile = new File(fileName);
			myFile.delete();
			bWriter = new BufferedWriter(new FileWriter(fileName, true));
			for (int i : map.keySet()) {
				ArrayList<Integer> al = new ArrayList<>();
				al = map.get(i);
				String oneLine = "";
				for (int j = 0; j < al.size(); ++j) {
					if (j == 0) {
						oneLine =
							"label        minRow         maxRow        minCol      maxCol       width     height    noOfPixel      CR        CC         RC        R2           C2         Vr         Vc     ";
						bWriter.write(oneLine);
						bWriter.newLine();
						bWriter.flush();
						oneLine = "";
					}
					oneLine += al.get(j) + " ";
				}
				bWriter.write(oneLine);
				bWriter.newLine();
				bWriter.flush();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class pixel {
	int col;
	int row;

	public pixel() {
		this.row = 0;
		this.col = 0;
	}

	public pixel(int r, int c) {
		this.row = r;
		this.col = c;
	}
}