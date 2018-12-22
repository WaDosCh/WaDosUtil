package ch.judos.generic.files.openImageDialog;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class OpenImageDialog {
	private File selectedFile;
	private boolean cancelled;

	public OpenImageDialog(Component parent, String title, File currentPath) {

		JFileChooser fc = new JFileChooser(currentPath);
		UIManager.put("FileChooser.openDialogTitleText", title);
		SwingUtilities.updateComponentTreeUI(fc);
		// Add a custom file filter and disable the default
		// (Accept All) file filter.
		fc.addChoosableFileFilter(new ImageFilter());
		fc.setAcceptAllFileFilterUsed(false);

		// Add custom icons for file types.
		// fc.setFileView(new ImageFileView());

		// Add the preview pane.
		fc.setAccessory(new ImagePreview(fc));

		// Show it.
		fc.setName(title);
		fc.setToolTipText(title);
		int returnVal = fc.showDialog(parent, null);
		this.cancelled = returnVal != JFileChooser.APPROVE_OPTION;

		this.selectedFile = fc.getSelectedFile();

		// Reset the file chooser for the next time it's shown.
		fc.setSelectedFile(null);
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public File getSelectedFile() {
		return this.selectedFile;
	}
}
