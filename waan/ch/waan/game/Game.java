package ch.waan.game;

import org.eclipse.jdt.annotation.NonNull;

import ch.waan.game.ui.UIComponent;
import ch.waan.game.ui.UIContainer;
import ch.wados.util.HighPrecisionClock;

/**
 * High-level game manager.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-03-19
 */
public final class Game {

	private @NonNull SceneController controller;
	private @NonNull final GUI gui;
	private @NonNull final HighPrecisionClock frameClock;
	private @NonNull final HighPrecisionClock tickClock;

	private Game(int width, int height, @NonNull SceneController controller, int tickRate,
		int frameRate) {
		this.gui = GUIImpl.newInstance(width, height, this::close);
		this.controller = controller;
		this.frameClock = new HighPrecisionClock(frameRate, this::render);
		this.tickClock = new HighPrecisionClock(tickRate, this::tick);
		this.controller.loadController(this.gui, this);
		this.frameClock.start();
		this.tickClock.start();
	}

	@SuppressWarnings("hiding")
	void loadScene(@NonNull SceneController controller) {
		if (controller == this.controller)
			throw new IllegalArgumentException("cannot load active controller");
		this.controller.onExit();
		// unload old stuff
		UIContainer<UIComponent> c = this.gui.getRootContainer();
		while (!this.gui.isEmpty()) {
			c.removeComponent(c.getComponents().get(0));
		}
		// load new stuff
		this.controller = controller;
		this.controller.loadController(this.gui, this);
	}

	void setTPS(int tps) {
		this.tickClock.setTPS(tps);
	}

	void setFPS(int fps) {
		this.frameClock.setTPS(fps);
	}

	int getTPS() {
		return this.tickClock.getTPS();
	}

	int getFPS() {
		return this.frameClock.getTPS();
	}

	private void tick(int passedTime) {
		this.controller.tick(passedTime);
	}

	@SuppressWarnings("unused")
	private void render(int passedTime) {
		this.gui.render();
	}

	private void close() {
		this.controller.close();
	}

	void terminate() {
		this.tickClock.stop();
		this.frameClock.stop();
		this.gui.dispose();
	}

	/**
	 * Creates a new Game from the given initial scene controller
	 * 
	 * @param initialScene
	 *            the initial scene
	 * @return the game instance
	 */
	public static @NonNull Game initializeGame(@NonNull SceneController initialScene) {
		return new Game(800, 600, initialScene, 20, 50);
	}
}
