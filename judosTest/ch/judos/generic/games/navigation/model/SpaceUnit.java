package ch.judos.generic.games.navigation.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ch.judos.generic.games.pathsearch.SimpleWayPoint;
import ch.judos.generic.games.pathsearch.WayPoint;
import ch.judos.generic.games.unitCoordination.ReservingUnitCoordination;

/**
 * @author Julian Schelker
 * @version 1.0
 * @dependsOn
 */
public class SpaceUnit extends ReservingUnitCoordination implements Unit {

	private boolean paused;
	public float speed;

	public SpaceUnit(Map m, int gridx, int gridy) {
		super(m, new SimpleWayPoint(gridx, gridy), new WaterFieldChecker(m));
		this.speed = 1;
		this.paused = false;
	}

	@Override
	protected float getSpeed() {
		if (this.paused)
			return 0;
		return this.speed;
	}

	@Override
	public void paint(Graphics g) {
		update();

		g.setColor(Color.green);

		for (int i = 0; i < this.targets.size(); i++) {
			Point t1 = this.map.getPointFromField(this.targets.get(i)).getPoint();
			if (i == 0)
				g.drawLine((int) getPosition().getX(), (int) getPosition().getY(), t1.x, t1.y);
			if (i < this.targets.size() - 1) {
				Point t2 = this.map.getPointFromField(this.targets.get(i + 1)).getPoint();
				g.drawLine(t1.x, t1.y, t2.x, t2.y);
			}
			g.fillRect(t1.x - 10, t1.y - 10, 20, 20);
		}

		g.setColor(new Color(80, 40, 0));
		int x = (int) getPosition().getX() - 25;
		int y = (int) getPosition().getY() - 25;
		g.fillRect(x, y, 50, 50);
		g.setColor(Color.white);
		g.drawString("uses", x + 5, y + 15);
		g.drawString("some", x + 5, y + 30);
		g.drawString("space", x + 5, y + 45);

		g.setColor(Color.red);
		if (!this.finalTargets.isEmpty()) {
			WayPoint f = this.finalTargets.get(0);
			Point p = this.map.getPointFromField(f).getPoint();
			int lx = p.x;
			int ly = p.y;
			g.drawLine(lx - 25, ly - 25, lx + 25, ly + 25);
			g.drawLine(lx + 25, ly - 25, lx - 25, ly + 25);
		}
	}

	@Override
	public void pauseOrContinue() {
		this.paused = !this.paused;
	}

}
