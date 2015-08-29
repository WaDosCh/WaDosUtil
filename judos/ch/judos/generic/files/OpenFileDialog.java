package ch.judos.generic.files;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import ch.judos.generic.files.openImageDialog.ImagePreview;

/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class OpenFileDialog {
	private boolean cancelled;
	private File selectedFile;

	public OpenFileDialog(Component parent, String title, File currentPath, String[] extensions) {
		JFileChooser fc = new JFileChooser(currentPath);
		UIManager.put("FileChooser.openDialogTitleText", title);
		SwingUtilities.updateComponentTreeUI(fc);
		// Add a custom file filter and disable the default
		// (Accept All) file filter.
		fc.addChoosableFileFilter(new DefinedFileFilter(extensions));
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

	private class DefinedFileFilter extends FileFilter {
		private String[] extensions;

		public DefinedFileFilter(String[] extensions) {
			this.extensions = extensions;
		}

		@Override
		public boolean accept(File file) {
			String ext = FileUtils.getExtension(file);
			for (String s : this.extensions)
				if (s.equals(ext))
					return true;
			return false;
		}

		@Override
		public String getDescription() {
			String s = this.extensions[0];
			for (int i = 1; i < this.extensions.length; i++)
				s = s + ", " + this.extensions[i];
			return s;
		}

	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public File getSelectedFile() {
		return this.selectedFile;
	}
}
