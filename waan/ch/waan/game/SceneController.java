package ch.waan.game;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.waan.game.ui.UIComponent;
import ch.waan.game.ui.UIContainer;

/**
 * Controller for a single high-level game state.
 * <p>
 * Scene Controllers are used to model a single high-level game state. Almost
 * always a game can be divided into a finite set of intrinsically different
 * states (e.g. main menu, main game view, pause screen, config screen) and a
 * finite set of possible transitions between these states. It is essential for
 * this model that all state transitions are initiated by the state that is
 * left. Therefore a state cannot be forced to give up its activity.
 * </p>
 * <p>
 * Although it not being a necessity for this model it is recommended to treat
 * state-internal data comparable to scope-limited (local) fields. I.e. it
 * should be avoided to be required to hold on to a left state. Adhesion to this
 * principle allows for a simpler state handling.
 * </p>
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-03-19
 */
public abstract class SceneController {

	private @Nullable GUI theGui;
	private @Nullable Game theLogic;

	void loadController(final @NonNull GUI gui, final @NonNull Game logic) {
		if (!gui.isEmpty())
			throw new IllegalStateException("GUI is not empty");
		this.theLogic = logic;
		this.theGui = gui;
		this.load(gui.getRootContainer());
	}

	/**
	 * asserts that the scene is currently loaded.
	 * 
	 * @throws IllegalArgumentException
	 *             if the scene is not loaded currently
	 */
	protected final void assertLoaded() {
		if (this.theGui == null || this.theLogic == null)
			throw new IllegalArgumentException("Scene is not loaded");
	}

	/**
	 * Initiates the transition to a new scene.
	 * <p>
	 * Internally all components present in the GUI are removed. This scene will
	 * then be silently unloaded, i.e. will stop receiving logic ticks. This
	 * scene instance will be dropped from the underlying logic. Therefore any
	 * relevant data present in the scene must be properly handled by the scene
	 * implementation itself.
	 * </p>
	 * When calling this method from a {@link SceneController#tick(int) tick},
	 * after returning from this method the tick method <i>must</i> be
	 * terminated directly.
	 * 
	 * @param newScene
	 *            the scene to transition to
	 * 
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 * @throws IllegalArgumentException
	 *             if the new Scene is identical to this one. A scene may never
	 *             load itself
	 */
	protected final void transitionTo(final @NonNull SceneController newScene) {
		assertLoaded();
		if (newScene == this)
			throw new IllegalArgumentException("Scene cannot transition to itself!");
		assert this.theLogic != null;
		this.theLogic.loadScene(newScene);
		this.theLogic = null;
		this.theGui = null;
	}

	void terminate() {
		assert this.theLogic != null;
		this.theLogic.terminate();
	}

	/**
	 * Returns the currently configured tick rate
	 * 
	 * @return the current tick rate in ticks-per-second
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 */
	protected final int getTickRate() {
		assertLoaded();
		assert this.theLogic != null;
		return this.theLogic.getTPS();
	}

	/**
	 * Returns the currently configured frame rate
	 * 
	 * @return the current frame rate in frames-per-second
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 */
	protected final int getFrameRate() {
		assertLoaded();
		assert this.theLogic != null;
		return this.theLogic.getFPS();
	}

	/**
	 * Sets the target tick rate.
	 * 
	 * A value set by this method will be preserved through transition to
	 * another scene.
	 * 
	 * @param tickRate
	 *            the new target tick rate. This value must be strictly positive
	 * 
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 * @throws IllegalArgumentException
	 *             if the given tickRate parameter is zero or negative
	 */
	protected final void setTickRate(int tickRate) {
		assertLoaded();
		if (tickRate <= 0)
			throw new IllegalArgumentException(
				"tick rate must be strictly positive. given value was " + tickRate);
		assert this.theLogic != null;
		this.theLogic.setTPS(tickRate);
	}

	/**
	 * Sets the target frame rate.
	 * 
	 * A value set by this method will be preserved through transition to
	 * another scene.
	 * 
	 * @param frameRate
	 *            the new target frame rate. This value must be strictly
	 *            positive
	 * 
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 * @throws IllegalArgumentException
	 *             if the given frameRate parameter is zero or negative
	 */
	protected final void setFrameRate(int frameRate) {
		assertLoaded();
		if (frameRate <= 0)
			throw new IllegalArgumentException(
				"frame rate must be strictly positive. given value was " + frameRate);
		assert this.theLogic != null;
		this.theLogic.setFPS(frameRate);
	}

	/**
	 * Returns the underlying GUI instance.
	 * 
	 * @return the GUI
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 */
	protected final @NonNull GUI gui() {
		assertLoaded();
		assert this.theGui != null;
		return this.theGui;
	}

	/**
	 * Returns the underlying GUI root container
	 * 
	 * @return the root container
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 * @since AwaeGameAPI 0.2
	 */
	protected final @NonNull UIContainer<UIComponent> root() {
		assertLoaded();
		assert this.theGui != null;
		return this.theGui.getRootContainer();
	}

	/**
	 * Returns the Keyboard instance of the underlying GUI.
	 * 
	 * The returned instance should <i>not</i> be shared with external
	 * components.
	 * 
	 * @return the GUI keyboard
	 * @throws IllegalStateException
	 *             if the scene is not loaded
	 */
	protected final @NonNull Keyboard keyboard() {
		assertLoaded();
		assert this.theGui != null;
		return this.theGui.getKeyboard();
	}

	/**
	 * Loads the initial UI elements associated with this scene.
	 * 
	 * This method is used by the underlying loading routine and should
	 * <i>never</i> be used otherwise.
	 * 
	 * @param root
	 *            the root container to load the elements into
	 * @since AwaeGameAPI 0.2
	 * 
	 * @implSpec The passed container should be used only during loading. In any
	 *           other situation the same object can be accessed using the
	 *           {@link SceneController#root() root()} getter method.
	 */
	protected abstract void load(@NonNull UIContainer<UIComponent> root);

	/**
	 * Performs a logic update tick on the scene
	 * 
	 * @param passedTime
	 *            the time since the last invocation of this method in
	 *            milliseconds. On the first invocation its value is not
	 *            precisely defined and may depend on external implementation
	 *            detail. The possibility of zero as a value must be considered
	 *            in any implementation.
	 * 
	 * @throws IllegalStateException
	 *             if the scene is not loaded.
	 * 
	 * @implSpec Any implementation must make sure that the scene is loaded. The
	 *           best approach is a head-call to
	 *           {@link SceneController#assertLoaded() assertLoaded()}
	 */
	protected abstract void tick(int passedTime);

	/**
	 * Allows exit handling.
	 * <p>
	 * This method is always invoked during the transition process. It will be
	 * called whilst the controller still owns the GUI. This method is designed
	 * for internal data cleanup and may not throw any exceptions.
	 * </p>
	 * 
	 * @since AwaeGameAPI 0.2
	 * @default by default this method is empty and therefore does nothing.
	 */
	protected void onExit() {
		// default: no action here
	}

	/**
	 * The Terminator scene.
	 * 
	 * Performing a transition to this scene will always terminate the
	 * application.
	 * 
	 * @implSpec the current implementation stops all internal clocks and
	 *           gracefully disposes the UI.
	 */
	protected final static @NonNull SceneController TERMINATOR_SCENE = new SceneController() {

		@Override
		protected void tick(int passedTime) {
			// no
			// action
		}

		@Override
		protected void load(UIContainer<UIComponent> root) {
			terminate();
			// System.exit(0);
		}

	};

	/**
	 * Is Invoked whenever the close button of the game window is pressed.
	 * 
	 * @default the default implementation quits the system through transition
	 *          to the {@link SceneController#TERMINATOR_SCENE terminator scene}
	 *          .
	 * @since AwaeGameAPI 0.2
	 */
	protected void close() {
		transitionTo(TERMINATOR_SCENE);
	}
}
