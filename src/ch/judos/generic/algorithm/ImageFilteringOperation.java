/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */
package ch.judos.generic.algorithm;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * 
 * 
 * @author me
 */
public class ImageFilteringOperation {
	public static ArrayList<Double> neighborList;
	public static double max = 0;

	/**
	 * 
	 * This method performs median filtering and returns an enhanced image.
	 * 
	 * @param _enlargeImage
	 *            image to enlarge
	 * 
	 * @param _desImage
	 *            result array where to save transformed image
	 * 
	 * @param dimension
	 *            which dimension to use for filtering
	 * 
	 * @return enhanced image double[][]
	 */
	public static double[][] medianFilteringOperation(double[][] _enlargeImage,
		double[][] _desImage, int dimension) {
		// int NoOfNeighbors = 0;
		double medianOfNeighbors = 0.0;
		if (dimension == 3) {
			// NoOfNeighbors = 9;
		}
		neighborList = new ArrayList<>();
		for (int i = 1; i < _enlargeImage.length - 1; ++i) {
			for (int j = 1; j < _enlargeImage[1].length - 1; ++j) {
				neighborList.add(_enlargeImage[i - 1][j - 1]);
				neighborList.add(_enlargeImage[i - 1][j]);
				neighborList.add(_enlargeImage[i - 1][j + 1]);
				neighborList.add(_enlargeImage[i][j - 1]);
				neighborList.add(_enlargeImage[i][j]);
				neighborList.add(_enlargeImage[i][j + 1]);
				neighborList.add(_enlargeImage[i + 1][j - 1]);
				neighborList.add(_enlargeImage[i + 1][j]);
				neighborList.add(_enlargeImage[i + 1][j + 1]);
				Collections.sort(neighborList);
				medianOfNeighbors = neighborList.get(4);
				if (i == 1 & j == 1) {
					max = medianOfNeighbors;
				}
				if (medianOfNeighbors > max) {
					max = medianOfNeighbors;
				}
				_desImage[i - 1][j - 1] = medianOfNeighbors;
				neighborList.clear();
			}
		}
		return _desImage;
	}

	/**
	 * this method use GaussianFiltering to enhance an image.
	 * 
	 * 1 2 1 a b c
	 * 
	 * 2 4 2 d e f
	 * 
	 * 1 2 1 g h k
	 * 
	 * 
	 * 
	 * @param _enlargeImage
	 *            image to filter
	 * 
	 * @param _desImage
	 *            where to store result
	 * 
	 * @param dimension
	 *            which dimension to be used for filtering
	 * 
	 * @return 2 dimension image
	 */
	public static double[][] gaussianFilteringOperation(double[][] _enlargeImage,
		double[][] _desImage, int dimension) {
		// int NoOfNeighbors = 0;
		double gaussianValue = 0.0;
		if (dimension == 3) {
			// NoOfNeighbors = 9;
		}
		for (int i = 1; i < _enlargeImage.length - 1; ++i) {
			for (int j = 1; j < _enlargeImage[0].length - 1; ++j) {
				double a = _enlargeImage[i - 1][j - 1];
				double b = _enlargeImage[i - 1][j];
				double c = _enlargeImage[i - 1][j + 1];
				double d = _enlargeImage[i][j - 1];
				double e = _enlargeImage[i][j];
				double f = _enlargeImage[i][j + 1];
				double g = _enlargeImage[i + 1][j - 1];
				double h = _enlargeImage[i + 1][j];
				double k = _enlargeImage[i + 1][j + 1];
				gaussianValue = (1 * a + 2 * b + 1 * c + 2 * d + 4 * e + 2 * f + 1 * g + 2 * h
					+ 1 * k) / 16.0;
				if (i == 1 & j == 1) {
					max = gaussianValue;
				}
				if (gaussianValue > max) {
					max = gaussianValue;
				}
				_desImage[i - 1][j - 1] = DistanceTransform.roundToDecimals(gaussianValue, 2);
			}
		}
		return _desImage;
	}
}