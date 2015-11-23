package ch.judos.generic.os;

import java.io.IOException;

import ch.judos.generic.os.RegistryException.RethrownRegistryException;

/**
 * @since 23.11.2015
 * @author Julian Schelker
 */
public class FileTypeAssociates {

	/**
	 * @param fileType
	 *            without dot e.g. "java"
	 * @param command
	 * @param fileDescription
	 * @return
	 */
	public void win_add(String fileType, String command, String fileDescription)
		throws RegistryException {

		try {
			WindowsRegistryNode node = new WindowsRegistryNode(
				"HKEY_CURRENT_USER\\Software\\Classes");
			WindowsRegistryNode fileNode = node.addSubNode("." + fileType);
			if (fileNode == null)
				throw new RegistryException("Could not create file node");
			if (!fileNode.setKeyValueREG_SZ("", fileType + "_file"))
				throw new RegistryException("Could not write default value");

			WindowsRegistryNode fileMeta = node.addSubNode(fileType + "_file");
			if (!fileMeta.setKeyValueREG_SZ("", fileDescription))
				throw new RegistryException("Could not write file description");
			WindowsRegistryNode commandNode = fileMeta.addSubNode("shell\\open\\command");
			if (!commandNode.setKeyValueREG_SZ("", command))
				throw new RegistryException("Could not write command");
		}
		catch (IOException e) {
			throw new RethrownRegistryException(
				"IOException occured while creating fileAssociation.", e);
		}
		catch (InterruptedException e) {
			throw new RethrownRegistryException(
				"InterruptedException occured while creating fileAssociation.", e);
		}
	}
}
