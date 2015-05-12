package ch.judos.generic.games.navigation.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ch.judos.generic.games.pathsearch.FreeFieldCheckAdapter;
import ch.judos.generic.games.pathsearch.GridMap;
import ch.judos.generic.games.pathsearch.SimpleWayPoint;
import ch.judos.generic.games.pathsearch.WayPoint;
import ch.judos.generic.games.unitCoordination.NoFreeSpaceException;
import ch.judos.generic.games.unitCoordination.NonReservingUnitCoordination;

/**
 * @since 27.07.2013
 * @author Julian Schelker
 */
public class FlyUnit extends NonReservingUnitCoordination implements Unit {

	private boolean	paused;
	private float	speed;

	public FlyUnit(GridMap map, int x, int y) throws NoFreeSpaceException {
		super(map, new SimpleWayPoint(x, y), new FlyUnitFieldChecker(map));
		this.paused = false;
		this.speed = 2f;
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
				g.drawLine((int) getPosition().getX(), (int) getPosition().getY(), t1.x,
					t1.y);
			if (i < this.targets.size() - 1) {
				Point t2 = this.map.getPointFromField(this.targets.get(i + 1)).getPoint();
				g.drawLine(t1.x, t1.y, t2.x, t2.y);
			}
			g.fillRect(t1.x - 10, t1.y - 10, 20, 20);
		}

		g.setColor(Color.lightGray);
		int x = (int) getPosition().getX() - 25;
		int y = (int) getPosition().getY() - 25;
		g.fillRect(x, y, 50, 50);
		g.setColor(Color.black);
		g.drawString("", x + 5, y + 15);
		g.drawString("FLYER", x + 5, y + 30);
		g.drawString("", x + 5, y + 45);

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

	private static class FlyUnitFieldChecker extends FreeFieldCheckAdapter {

		public FlyUnitFieldChecker(GridMap map) {
			super(map);
		}

		@Override
		public boolean isFree(WayPoint p) {
			return true;
		}

	}

}
