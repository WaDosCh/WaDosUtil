package ch.judos.generic.games;

import java.awt.Dimension;
import java.awt.Point;

/**
 * this class saves the current scrolling on the map
 * 
 * @since 05.12.2011
 * @author Julian Schelker , Roger Kohler
 * @version 1.01 / 17.02.2013
 */
public abstract class Scroll extends BorderlessScrolling {

	protected Dimension terrainSize;

	/**
	 * initialize
	 * 
	 * @param terrainSize
	 *            in pixels
	 * @param viewPort
	 *            size of the viewport in pixel
	 */
	public Scroll(Dimension terrainSize, Dimension viewPort) {
		super(viewPort);
		this.locX = 0;
		this.locY = 0;
		this.terrainSize = terrainSize;

	}

	@Override
	public void update() {
		Point point = getMousePoint();

		if (this.viewPort.width < this.terrainSize.width)
			scrollH(point, this.viewPort);
		else
			centerH(this.viewPort);

		if (this.viewPort.height < this.terrainSize.height)
			scrollV(point, this.viewPort);
		else
			centerV(this.viewPort);
	}

	void centerH(Dimension size) {
		this.locX = this.terrainSize.width / 2 - size.width / 2;
	}

	void centerV(Dimension size) {
		this.locY = this.terrainSize.height / 2 - size.height / 2;
	}

}
