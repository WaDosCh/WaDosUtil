package ch.judos.generic.os;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.judos.generic.data.MutableBoolean;
import ch.judos.generic.data.StringUtils;

/**
 * TODO: make sure exceptions are thrown correctly and no null values are
 * returned instead
 * 
 * @since 23.11.2015
 * @author Julian Schelker
 */
public class WindowsRegistryNode {

	public static void main(String[] args) throws IOException, InterruptedException,
		RegistryException {

		WindowsRegistryNode node = new WindowsRegistryNode(
			"HKEY_CURRENT_USER\\Software\\Classes\\java_file");

		// show all subnodes of java_file
		System.out.println(node.subnodes());

		// add a subnode called test
		WindowsRegistryNode node2 = node.addSubNode("test");

		// show all key value pairs for this node
		System.out.println(node2.keyValues());

		// modify key value pairs
		node2.setKeyValueREG_SZ("key", "value");
		node2.deleteKeyValue("key2");

		System.out.println(node2.keyValues());

		// delete the test node again
		node2.deleteNode();

		// show the super node of java_file
		System.out.println(node.superNode());

		// create a chained sub node
		WindowsRegistryNode newNode = node.addSubNode("newTest\\hello\\World\\a");

		// set the value for the empty/(Standard) key
		newNode.setKeyValueREG_SZ("", "default value");
	}

	/**
	 * path without \ at the end
	 */
	private String path;

	public WindowsRegistryNode(String path) throws IOException, InterruptedException,
		RegistryException {
		this.path = path;
		if (!this.nodeExists())
			throw new RegistryException("Registry node does not exist");
	}

	private WindowsRegistryNode() {
		// private constructor to create keys without check
	}

	public WindowsRegistryNode superNode() throws IOException, InterruptedException,
		RegistryException {
		String newPath = this.path;
		int index = newPath.lastIndexOf("\\");
		if (index == -1)
			return null;

		newPath = newPath.substring(0, index);
		return new WindowsRegistryNode(newPath);
	}

	public boolean deleteNode() throws IOException, InterruptedException {
		// uses /f to force override of value if existing
		String cmd = "reg delete \"" + this.path + "\" /f";
		MutableBoolean hasError = new MutableBoolean(false);
		MutableBoolean seemsOk = new MutableBoolean(false);
		Commands.runCommand(cmd, (String s) -> {
			seemsOk.state = true;
		}, (String error) -> {
			System.err.println("ERR: " + error);
			hasError.state = true;
		}, true);
		return !hasError.state && seemsOk.state;
	}

	public boolean deleteKeyValue(String key) throws IOException, InterruptedException {
		// uses /f to force override of value if existing
		String cmd = "reg delete \"" + this.path + "\" /v \"" + key + "\" /f";
		MutableBoolean hasError = new MutableBoolean(false);
		MutableBoolean seemsOk = new MutableBoolean(false);
		Commands.runCommand(cmd, (String s) -> {
			seemsOk.state = true;
		}, (String error) -> {
			System.err.println("ERR: " + error);
			hasError.state = true;
		}, true);

		return !hasError.state && seemsOk.state;
	}

	public boolean setKeyValueREG_SZ(String key, String value) throws IOException,
		InterruptedException {
		value = StringUtils.replaceAll(value, new String[]{"\""}, "\\\"");
		// uses /f to force override of value if existing
		String cmd = "reg add \"" + this.path + "\" /v \"" + key + "\" /t REG_SZ /d \""
			+ value + "\" /f";
		MutableBoolean hasError = new MutableBoolean(false);
		MutableBoolean seemsOk = new MutableBoolean(false);
		Commands.runCommand(cmd, (String s) -> {
			seemsOk.state = true;
		}, (String error) -> {
			System.err.println("ERR: " + error);
			hasError.state = true;
		}, true);

		return !hasError.state && seemsOk.state;
	}

	public WindowsRegistryNode addSubNode(String name) throws IOException,
		InterruptedException, RegistryException {
		String newPath = this.path + "\\" + name;
		// uses /f to force override of value if existing
		String cmd = "reg add \"" + newPath + "\" /f";
		MutableBoolean hasError = new MutableBoolean(false);
		MutableBoolean seemsOk = new MutableBoolean(false);
		StringBuffer errorBuf = new StringBuffer();
		Commands.runCommand(cmd, (String s) -> {
			seemsOk.state = true;
		}, (String error) -> {
			errorBuf.append(error + "\n");
			hasError.state = true;
		}, true);
		if (hasError.state || !seemsOk.state) {
			throw new RegistryException(errorBuf.toString());
		}
		WindowsRegistryNode node = new WindowsRegistryNode();
		node.path = newPath;
		return node;
	}

	public HashMap<String, String> keyValues() {
		HashMap<String, String> map = new HashMap<>();

		MutableBoolean keyValueStarted = new MutableBoolean(false);
		try {
			Commands.runCommand("reg query \"" + this.path + "\" /se #", (String s) -> {

				if (keyValueStarted.state && s.startsWith(this.path)) {
					keyValueStarted.state = false;
				}
				if (keyValueStarted.state) {
					Matcher m = Pattern.compile(" {4}(.+) {4}.+ {4}(.+)").matcher(s);
					if (m.matches()) {
						String key = m.group(1);
						if (key.equals("(Standard)"))
							key = "";
						map.put(key, m.group(2));
					}
				}
				if (s.equals(this.path) && !keyValueStarted.state) {
					keyValueStarted.state = true;
				}

			}, (String error) -> {
				System.err.println(error);
			}, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public List<WindowsRegistryNode> subnodes() {
		ArrayList<WindowsRegistryNode> result = new ArrayList<>();
		try {
			Commands.runCommand("reg query \"" + this.path + "\" /se #", (String s) -> {
				if (s.startsWith(this.path) && !s.equals(this.path)) {
					WindowsRegistryNode node = new WindowsRegistryNode();
					node.path = s;
					result.add(node);
				}
			}, (String error) -> {
				System.err.println(error);
			}, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean nodeExists() throws IOException, InterruptedException {
		MutableBoolean hasError = new MutableBoolean(false);
		MutableBoolean foundPath = new MutableBoolean(false);
		Commands.runCommand("reg query \"" + this.path + "\" /v \"\"", (String s) -> {
			if (s.equals(this.path))
				foundPath.state = true;
		}, (String error) -> {
			hasError.state = true;
		}, true);
		return !hasError.state && foundPath.state;
	}

	public String getPath() {
		return this.path;
	}

	@Override
	public String toString() {
		return getPath();
	}

}
