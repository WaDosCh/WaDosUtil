package ch.judos.generic.data;

public enum OSType {

	WINDOWS, MAC, UNIX, SOLARIS, UNKNOWN;

	private static String OS = System.getProperty("os.name", "generic").toLowerCase();

	public static OSType detect() {
		if (isMac())
			return MAC;
		if (isWindows())
			return WINDOWS;
		if (isUnix())
			return UNIX;
		if (isSolaris())
			return SOLARIS;
		return UNKNOWN;
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return OS.indexOf("mac") >= 0 || OS.indexOf("darwin") >= 0;
	}

	public static boolean isUnix() {
		return OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0;
	}

	public static boolean isSolaris() {
		return OS.indexOf("sunos") >= 0;
	}
}
