package ch.judos.generic.graphics.fullscreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * @since 05.09.2011
 * @author Julian Schelker
 * @version 1.15 / 21.02.2013
 */
public class FullScreenDialogComplex extends JDialog implements ActionListener,
	ListSelectionListener, FullScreenDialogI {

	private static final long serialVersionUID = -7452838983112945857L;
	JButton exit;
	JButton ok;
	private JTable dmList;
	private JScrollPane dmPane;
	private GraphicsDevice[] devices;

	// java 6 vs java 7 problem with parametrized JComboBox
	@SuppressWarnings("rawtypes")
	private JComboBox gui_devices;
	private DisplayMode result;
	private GraphicsDevice resultD;
	private boolean chosen;
	private KeyEventDispatcher dispatcher;

	/**
	 * index of width column
	 */
	public static final int INDEX_WIDTH = 0;
	/**
	 * index of height column
	 */
	public static final int INDEX_HEIGHT = 1;
	/**
	 * index of bitdepth column
	 */
	public static final int INDEX_BITDEPTH = 2;
	/**
	 * index of refreshrate column
	 */
	public static final int INDEX_REFRESHRATE = 3;

	/**
	 * width of columns
	 */
	public static final int[] COLUMN_WIDTHS = new int[]{100, 100, 100, 100};
	/**
	 * names of columns
	 */
	public static final String[] COLUMN_NAMES = new String[]{"Width", "Height", "Bit Depth",
		"Refresh Rate"};

	FullScreenDialogComplex() {
		this("Select Display Mode");
	}

	FullScreenDialogComplex(String title) {
		super();

		this.chosen = false;
		this.devices = FullScreen.getDevices();
		setTitle(title);

		initComponents();
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		setLocationRelativeTo(null);
		addKeyBoardHandling();

		setVisible(true);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource();
		if (source == this.exit) {
			quit();

		}
		else if (source == this.gui_devices) {
			JS_GraphicsDevice jsdev = (JS_GraphicsDevice) this.gui_devices.getSelectedItem();
			DisplayModeModel model = new DisplayModeModel(FullScreen
				.getDisplayModesSorted(jsdev.dev));
			this.dmList.setModel(model);
			this.dmList.getSelectionModel().setSelectionInterval(0, 0);
			this.result = model.getDisplayMode(0);

			this.resultD = jsdev.dev;
			this.ok.setEnabled(true);
		}
		else if (source == this.ok) {
			this.chosen = true;
			quit();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.graphics.fullscreen.FullScreenDialogI#getDisplayMode()
	 */
	@Override
	public DisplayMode getDisplayMode() {
		return this.chosen ? this.result : null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.graphics.fullscreen.FullScreenDialogI#getGraphicsDevice()
	 */
	@Override
	public GraphicsDevice getGraphicsDevice() {
		return this.chosen ? this.resultD : null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.graphics.fullscreen.FullScreenDialogI#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return !this.chosen;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 *      .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		int index = this.dmList.getSelectionModel().getAnchorSelectionIndex();
		if (index >= 0) {
			DisplayModeModel model = (DisplayModeModel) this.dmList.getModel();
			this.result = model.getDisplayMode(index);
			this.ok.setEnabled(true);
		}
	}

	private void addKeyBoardHandling() {
		this.dispatcher = new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean keyHandled = false;
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						actionPerformed(new ActionEvent(FullScreenDialogComplex.this.ok, 0,
							"enter"));
						keyHandled = true;
					}
					else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						actionPerformed(new ActionEvent(FullScreenDialogComplex.this.exit, 0,
							"escape"));
						keyHandled = true;
					}
				}
				return keyHandled;
			}
		};
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
			this.dispatcher);
	}

	// java 6 vs java 7 problem with parametrized JComboBox
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void initComponents() {
		Container co = getContentPane();

		co.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 0;

		// Text
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		co.add(new JLabel("Select the device / screen to use:"), c);

		// Devices
		c.gridy++;
		c.anchor = GridBagConstraints.CENTER;
		JS_GraphicsDevice[] dev = new JS_GraphicsDevice[this.devices.length];
		for (int i = 0; i < dev.length; i++) {
			dev[i] = new JS_GraphicsDevice(this.devices[i]);
		}

		this.gui_devices = new JComboBox(dev);
		this.gui_devices.setSelectedItem(dev[0]);
		this.resultD = dev[0].dev;
		this.gui_devices.addActionListener(this);
		co.add(this.gui_devices, c);

		// Display Modes
		c.gridy++;
		c.weighty = 1;
		this.dmList = new JTable();
		DisplayModeModel model = new DisplayModeModel(FullScreen
			.getDisplayModesSorted(this.devices[0]));
		this.dmList.setModel(model);

		this.dmList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.dmList.getSelectionModel().setSelectionInterval(0, 0);

		this.result = model.getDisplayMode(0);
		this.dmList.getSelectionModel().addListSelectionListener(this);

		this.dmPane = new JScrollPane(this.dmList);
		// this.dmPane.setPreferredSize(new Dimension(350, 500));
		this.dmPane.setMinimumSize(new Dimension(350, 500));
		co.add(this.dmPane, c);

		// Buttons

		// Ausw�hlen
		c.gridy++;
		c.gridwidth = 1;
		c.weighty = 0;

		this.ok = new JButton("Select");
		co.add(this.ok, c);
		this.ok.addActionListener(this);

		// Abbrechen
		c.gridx = 1;
		this.exit = new JButton("Cancel");
		this.exit.addActionListener(this);
		co.add(this.exit, c);
	}

	private void quit() {
		removeKeyBoardHandling();
		dispose();
	}

	private void removeKeyBoardHandling() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(
			this.dispatcher);
	}

	private class JS_GraphicsDevice {

		GraphicsDevice dev;

		public JS_GraphicsDevice(GraphicsDevice dev) {
			this.dev = dev;
		}

		@Override
		public String toString() {
			return this.dev.getIDstring() + "  max: "
				+ FullScreen.getMaxResolutionString(this.dev);
		}
	}

	// private void setDMLabel(DisplayMode newMode) {
	// int bitDepth = newMode.getBitDepth();
	// int refreshRate = newMode.getRefreshRate();
	// String bd, rr;
	// if (bitDepth == DisplayMode.BIT_DEPTH_MULTI) {
	// bd = "Multi";
	// } else {
	// bd = Integer.toString(bitDepth);
	// }
	// if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
	// rr = "Unknown";
	// } else {
	// rr = Integer.toString(refreshRate);
	// }
	// currentDM.setText(COLUMN_NAMES[INDEX_WIDTH] + ": " + newMode.getWidth()
	// + " " + COLUMN_NAMES[INDEX_HEIGHT] + ": " + newMode.getHeight()
	// + " " + COLUMN_NAMES[INDEX_BITDEPTH] + ": " + bd + " "
	// + COLUMN_NAMES[INDEX_REFRESHRATE] + ": " + rr);
	// }

}

class DisplayModeModel extends DefaultTableModel {

	private static final long serialVersionUID = 3497527378948646452L;
	private DisplayMode[] modes;

	public DisplayModeModel(DisplayMode[] modes) {
		this.modes = modes;
	}

	@Override
	public int getColumnCount() {
		return FullScreenDialogComplex.COLUMN_WIDTHS.length;
	}

	@Override
	public String getColumnName(int c) {
		return FullScreenDialogComplex.COLUMN_NAMES[c];
	}

	public DisplayMode getDisplayMode(int r) {
		return this.modes[r];
	}

	@Override
	public int getRowCount() {
		if (this.modes == null) {
			return 0;
		}
		return this.modes.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		DisplayMode dm = this.modes[rowIndex];
		switch (colIndex) {
			case FullScreenDialogComplex.INDEX_WIDTH :
				return Integer.toString(dm.getWidth());
			case FullScreenDialogComplex.INDEX_HEIGHT :
				return Integer.toString(dm.getHeight());
			case FullScreenDialogComplex.INDEX_BITDEPTH : {
				int bitDepth = dm.getBitDepth();
				String ret;
				if (bitDepth == DisplayMode.BIT_DEPTH_MULTI) {
					ret = "Multi";
				}
				else {
					ret = Integer.toString(bitDepth);
				}
				return ret;
			}
			case FullScreenDialogComplex.INDEX_REFRESHRATE : {
				int refreshRate = dm.getRefreshRate();
				String ret;
				if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
					ret = "Unknown";
				}
				else {
					ret = Integer.toString(refreshRate);
				}
				return ret;
			}
			default :
				new Exception("Unknown FullScreenDialogComplex index: " + colIndex)
					.printStackTrace();
				break;
		}
		throw new ArrayIndexOutOfBoundsException("Invalid column value");
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}

}