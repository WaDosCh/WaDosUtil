package ch.judos.generic.network.udp.model;

import java.util.LinkedList;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class SpeedMeasurement {

	private static final int AVG_OVER_MEASUREMENTS = 20;
	private long currentValue;
	private long lastUpdate;
	private LinkedList<Float> speedValues;
	private double sum;

	public SpeedMeasurement() {
		this.currentValue = 0;
		this.lastUpdate = System.currentTimeMillis();
		this.speedValues = new LinkedList<>();
		this.sum = 0;
	}

	public float update(long currentPos) {
		long diff = currentPos - this.currentValue;
		long time = System.currentTimeMillis() - this.lastUpdate;
		// 1000*x since the time is given in ms
		float speed = (float) ((double) 1000 * diff / time);
		this.speedValues.addLast(speed);
		this.sum += speed;
		if (this.speedValues.size() > AVG_OVER_MEASUREMENTS)
			this.sum -= this.speedValues.removeFirst();

		this.lastUpdate = System.currentTimeMillis();
		this.currentValue = currentPos;

		return (float) (this.sum / this.speedValues.size());
	}

}
