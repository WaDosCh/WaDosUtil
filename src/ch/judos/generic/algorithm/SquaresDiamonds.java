package ch.judos.generic.algorithm;

import java.util.Random;

/**
 * implements the SquaresAndDiamonds algorithm to generate random terrains
 * 
 * @since ??.??.2009
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 */
public class SquaresDiamonds {

	private int segments;
	private float[][] points;
	private int currentSize;
	private float roughness;
	private int currentIteration;
	private int maxIterations;
	private Random randomGenerator;
	private float waterMin;

	/**
	 * @param iterations
	 *            how many iterations to do. defines size of array to be
	 *            returned
	 * @param water
	 *            minimum terrain level [0,1]
	 */
	public SquaresDiamonds(int iterations, float water) {
		this.waterMin = water;
		this.randomGenerator = new Random();
		// this.randomGenerator.setSeed(3);

		this.maxIterations = iterations;
		this.segments = (int) Math.pow(2, iterations) + 1;

		// initialize points
		this.points = new float[this.segments][this.segments];
		for (int x = 0; x < this.segments; x++) {
			for (int y = 0; y < this.segments; y++) {
				this.points[x][y] = -1;
			}
		}
		this.currentSize = this.segments - 1;
		this.roughness = 1f;
		this.currentIteration = 0;

		init();
		for (int i = 0; i < 10; i++) {
			smooth(1f);
		}

		restoreMaxHeight();
	}

	private void smooth(float smoothness) {
		float[][] newPoints = new float[this.segments][this.segments];
		this.currentSize = 1;

		for (int x = 0; x < this.segments; x++) {
			for (int y = 0; y < this.segments; y++) {

				float avg = avgNeighbours2(x, y);

				float newHeight = (1f - smoothness) * this.points[x][y] + smoothness * avg;
				// System.out.println("h�he: "+this.points[x][y]+" avg umg.:
				// "+avg+" neu:"+newHeight);

				newPoints[x][y] = newHeight;

			}
		}
		this.points = newPoints;

	}

	private void restoreMaxHeight() {
		float maxheight = -1;
		float minheight = 2;
		for (int x = 0; x < this.segments; x++) {
			for (int y = 0; y < this.segments; y++) {
				if (this.points[x][y] > maxheight)
					maxheight = this.points[x][y];
				if (this.points[x][y] < minheight)
					minheight = this.points[x][y];
			}
		}
		float factor = 1 / (maxheight - minheight);

		for (int x = 0; x < this.segments; x++) {
			for (int y = 0; y < this.segments; y++) {
				this.points[x][y] = (this.points[x][y] - minheight) * factor;

				if (this.points[x][y] < this.waterMin) {
					this.points[x][y] = this.waterMin;
				}
			}
		}
	}

	private void init() {

		while (this.currentIteration < this.maxIterations) {

			// start with edge squares
			if (this.currentIteration == 0) {
				this.points[0][0] = rndHeight(this.roughness, this.roughness);
				this.points[this.currentSize][0] = rndHeight(this.roughness, this.roughness);
				this.points[0][this.currentSize] = rndHeight(this.roughness, this.roughness);
				this.points[this.currentSize][this.currentSize] = rndHeight(this.roughness,
					this.roughness);
			}

			// diamond step
			int start = this.currentSize / 2;
			for (int x = start; x < this.segments; x += this.currentSize) {
				for (int y = start; y < this.segments; y += this.currentSize) {
					float ref = avgNeighbours1(x, y);
					this.points[x][y] = rndHeight(ref, this.roughness);
				}
			}

			// square step
			this.currentSize /= 2;
			for (int x = 0; x < this.segments; x += this.currentSize) {
				for (int y = 0; y < this.segments; y += this.currentSize) {
					if (this.points[x][y] == -1) {
						float ref = avgNeighbours2(x, y);
						this.points[x][y] = rndHeight(ref, this.roughness);
					}
				}
			}

			// do next iteration
			this.roughness /= 2;
			this.currentIteration++;
		}
	}

	private float avgNeighbours1(int x, int y) {
		int anz = 0;
		float value = 0;
		int ab = this.currentSize / 2;
		if (reachable(x + ab, y + ab)) {
			anz++;
			value += this.points[x + ab][y + ab];
		}
		if (reachable(x - ab, y + ab)) {
			anz++;
			value += this.points[x - ab][y + ab];
		}
		if (reachable(x + ab, y - ab)) {
			anz++;
			value += this.points[x + ab][y - ab];
		}
		if (reachable(x - ab, y - ab)) {
			anz++;
			value += this.points[x - ab][y - ab];
		}
		if (anz > 0) {
			value /= anz;
		}
		if (anz < 4)
			System.err.println("Error: this point " + x + "," + y
				+ " has not 4 neighbours...");
		return value;
	}

	private float avgNeighbours2(int x, int y) {
		int anz = 0;
		float value = 0;
		int ab = this.currentSize;
		if (reachable(x + ab, y)) {
			anz++;
			value += this.points[x + ab][y];
		}
		if (reachable(x - ab, y)) {
			anz++;
			value += this.points[x - ab][y];
		}
		if (reachable(x, y - ab)) {
			anz++;
			value += this.points[x][y - ab];
		}
		if (reachable(x, y + ab)) {
			anz++;
			value += this.points[x][y + ab];
		}
		if (anz > 0) {
			value /= anz;
		}
		return value;
	}

	private boolean reachable(int x, int y) {
		return (x >= 0 && x < this.segments && y >= 0 && y < this.segments);
	}

	private float rndHeight(float reference, float random) {
		return reference + (float) ((this.randomGenerator.nextDouble() * 2 - 1) * random);
	}

	/**
	 * @return the terrain array (it is a square)
	 */
	public float[][] getArray() {
		return this.points;
	}

	/**
	 * @return size of the array
	 */
	public int getSegments() {
		return this.segments;
	}

}
