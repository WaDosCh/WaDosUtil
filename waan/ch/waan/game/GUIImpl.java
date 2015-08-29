package ch.waan.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.waan.game.ui.UIComponent;
import ch.waan.game.ui.UIContainer;
import ch.waan.game.ui.UIResizableContainer;
import ch.waan.game.ui.templates.UIPane;

/**
 * GUI implementation
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-27
 */
class GUIImpl implements GUI {

	private static @NonNull GraphicsDevice getDefaultGraphicsDevice() {
		GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice();
		if (dev == null)
			throw new AssertionError(); // should never happen
		return dev;
	}

	private boolean fullscreen = false;

	private @NonNull GraphicsDevice gdevice = getDefaultGraphicsDevice();
	private final @NonNull JFrame frame;
	private final @NonNull UIResizableContainer<UIComponent> uiRoot;
	private final @NonNull BufferStrategy buffer;
	private final @NonNull ConcurrentLinkedQueue<@Nullable MouseAction> mouseActions;
	private final @NonNull Keyboard keyboard;

	private final @NonNull Insets windowInset;

	private GUIImpl(final @NonNull JFrame frame,
		final @NonNull UIResizableContainer<UIComponent> uiRoot,
		final @NonNull ConcurrentLinkedQueue<@Nullable MouseAction> mouseQueue,
		final @NonNull Keyboard keyboard) {
		this.frame = frame;
		this.uiRoot = uiRoot;
		this.keyboard = keyboard;
		final BufferStrategy b = frame.getBufferStrategy();
		if (b == null)
			throw new IllegalStateException(
				"frame could not be initialised for double buffering");
		this.buffer = b;
		final Insets inset = this.frame.getInsets();
		assert inset != null;
		this.insets = this.windowInset = inset;
		this.mouseActions = mouseQueue;
	}

	@Override
	public boolean isFullscreen() {
		return this.fullscreen;
	}

	@Override
	public void setFullscreen(final boolean fullscreen) {
		if (this.fullscreen == fullscreen)
			return;
		this.fullscreen = fullscreen;
		this.frame.setResizable(!fullscreen);
		this.gdevice.setFullScreenWindow(fullscreen ? this.frame : null);
		this.insets = fullscreen ? new Insets(0, 0, 0, 0) : this.windowInset;
	}

	@Override
	public GraphicsDevice getGraphicsDevice() {
		return this.gdevice;
	}

	@Override
	public void setGraphicsDevice(final @NonNull GraphicsDevice device) {
		assert device != null;
		if (this.fullscreen)
			throw new IllegalStateException(
				"graphics device cannot be changed while in fullscreen mode");
		this.gdevice = device;
	}

	@Override
	public UIContainer<UIComponent> getRootContainer() {
		return this.uiRoot;
	}

	@Override
	public void dispose() {
		this.setFullscreen(false);
		this.frame.setVisible(false);
		this.frame.dispose();
	}

	@Override
	public void setTitle(final @NonNull String title) {
		this.frame.setTitle(title);
	}

	private @NonNull Insets insets;

	@Override
	public void render() {
		// show last frame at beginning to ensure constant frame-rate
		this.buffer.show();
		Toolkit.getDefaultToolkit().sync();
		// update mouse position
		updateMouse();
		// draw next frame
		Graphics2D g = (Graphics2D) this.buffer.getDrawGraphics();
		g.translate(this.insets.left, this.insets.top);
		Dimension d = this.uiRoot.getDimension();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, d.width(), d.height());
		g.translate(d.bottomRight().x(), d.bottomRight().y());
		this.uiRoot.render(g);
		g.dispose();
	}

	private boolean hasMouse = false;

	private void updateMouse() {

		// process mouse movement
		java.awt.Point mousePoint = this.frame.getMousePosition();

		if (mousePoint == null) {
			if (this.hasMouse) {
				this.hasMouse = false;
				this.uiRoot.mouseLeft();
			}
		}
		else {
			Point mouse = Point.get(mousePoint.x - this.insets.left,
				mousePoint.y - this.insets.top)
				.translate(this.uiRoot.getDimension().topLeft());
			if (this.uiRoot.collides(mouse)) {
				if (!this.hasMouse) {
					this.hasMouse = true;
					this.uiRoot.mouseEntered();
				}
				this.uiRoot.mouseMoved(mouse);
			}
			else {
				this.hasMouse = false;
				this.uiRoot.mouseLeft();
			}
		}
		// process event queue

		@Nullable
		MouseAction action = null;
		while ((action = this.mouseActions.poll()) != null) {
			if (action.isButtonAction()) {
				if (action.isButtonDownAction()) {
					if (this.hasMouse)
						this.uiRoot.mouseButtonPressed(action.getButton());
				}
				else {
					this.uiRoot.mouseButtonReleased(action.getButton());
				}
			}
			else {
				if (this.hasMouse)
					this.uiRoot.mouseWheelScrolled(action.getScroll());
			}
		}

	}

	void updateRootSize() {
		Dimension d = Dimension
			.get(this.frame.getWidth() - this.insets.left - this.insets.right, this.frame
				.getHeight()
				- this.insets.top - this.insets.bottom);
		this.uiRoot.setDimension(d);
	}

	@Override
	public @NonNull Keyboard getKeyboard() {
		return this.keyboard;
	}

	private static class MouseAction {

		private final @Nullable MouseButton button;
		private final boolean isDown;
		private final double scroll;

		MouseAction(final @NonNull MouseButton button, final boolean isDown) {
			this.button = button;
			this.isDown = isDown;
			this.scroll = 0;
		}

		MouseAction(final double scroll) {
			this.button = null;
			this.isDown = false;
			this.scroll = scroll;
		}

		boolean isButtonAction() {
			return this.button != null;
		}

		boolean isButtonDownAction() {
			if (this.button != null)
				return this.isDown;
			throw new IllegalStateException("Mouse Action is a scroll action");
		}

		@NonNull
		MouseButton getButton() {
			MouseButton btn = this.button;
			if (btn == null)
				throw new IllegalStateException("Mouse Action is a scroll action");
			return btn;
		}

		double getScroll() {
			if (this.button != null)
				throw new IllegalStateException("Mouse Action is a button action");
			return this.scroll;
		}
	}

	static @NonNull GUI newInstance(final int width, final int height, Runnable closeAction) {
		if (width <= 0)
			throw new IllegalArgumentException("GUI width must be positive. value was "
				+ width);
		if (height <= 0)
			throw new IllegalArgumentException("GUI height must be positive. value was "
				+ height);
		Objects.requireNonNull(closeAction, "null exit handler not allowed");
		if (GraphicsEnvironment.isHeadless())
			throw new HeadlessException();
		JFrame frame = new JFrame();
		frame.setSize(width, height);
		frame.setIgnoreRepaint(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.createBufferStrategy(2);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeAction.run();
			}
		});
		frame.setFocusTraversalKeysEnabled(false);
		Insets inset = frame.getInsets();
		Dimension dim = Dimension.get(width - inset.left - inset.right, height - inset.top
			- inset.bottom);
		UIPane root = new UIPane(Point.get(0, 0), dim);

		ConcurrentLinkedQueue<@Nullable MouseAction> queue = new ConcurrentLinkedQueue<>();

		GUIImpl gui = new GUIImpl(frame, root, queue, KeyboardFactory.createKeyboard(frame));

		// BIND ACTION LISTENERS
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e == null)
					throw new AssertionError(); // should never happen
				queue.add(new MouseAction(MouseButton.getButtonForEvent(e), false));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e == null)
					throw new AssertionError(); // should never happen
				queue.add(new MouseAction(MouseButton.getButtonForEvent(e), true));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				root.mouseLeft();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				root.mouseEntered();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// handled properly by press / release
			}
		});
		frame.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e == null)
					throw new AssertionError(); // should never happen
				queue.add(new MouseAction(e.getPreciseWheelRotation()));
			}
		});
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// no action
			}

			@Override
			public void componentResized(ComponentEvent e) {
				gui.updateRootSize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// no action
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// no action
			}
		});
		// build GUI
		return gui;
	}

	@Override
	public boolean isEmpty() {
		return this.uiRoot.getComponents().isEmpty();
	}

}
