package ch.judos.generic.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * @since 16.02.2012
 * @author Julian Schelker
 * @version 1.01 / 22.02.2013
 */
public class IP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] ips = getLocalIpsAsStrings();
		System.out.println("Local adresses: " + ips.length);
		for (String ip : ips)
			System.out.println(ip);

		System.out.println("WAN IP: " + getWanIp());
		System.out.println("Behind NAT: " + isBehindNat());
	}

	private static String wanIp;

	/**
	 * <b>Note:</b> check url and server page to ensure this method works
	 * 
	 * @return the official ip address of your computer / nat device
	 */
	public static String getWanIp() {
		if (wanIp == null || "-not found-".equals(wanIp)) {
			wanIp = "-not found-";
			try {
				URL URL = new URL("https://api.ipify.org/");
				HttpURLConnection c = (HttpURLConnection) URL.openConnection();
				InputStream in = c.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				wanIp = br.readLine();
				br.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wanIp;
	}

	/**
	 * @return true if a nat device hides your official address
	 * @see #getWanIp()
	 */
	public static boolean isBehindNat() {
		boolean ok = true;
		String[] ips = getLocalIpsAsStrings();
		String wan = getWanIp();
		for (String ip : ips) {
			if (ip.equals(wan)) {
				ok = false;
				break;
			}
		}
		return ok;
	}

	/**
	 * @return all local ip-adresses of your computer as string
	 */
	public static String[] getLocalIpsAsStrings() {
		InetAddress[] ips = getLocalIps();
		String[] result = new String[ips.length];
		int index = 0;
		for (InetAddress ip : ips)
			result[index++] = ip.getHostAddress();
		return result;
	}

	/**
	 * @return all local ip-adresses
	 */
	public static InetAddress[] getLocalIps() {
		try {
			String localHost = InetAddress.getLocalHost().getHostName();
			return InetAddress.getAllByName(localHost);
		}
		catch (Exception e) {
			return null;
		}
	}
}
