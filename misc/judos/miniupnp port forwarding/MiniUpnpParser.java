package controller.portforwarding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Julian Schelker
 * @lastupdate 23.04.2012
 */
public class MiniUpnpParser {
	
	public static String waitToTerminateAndGetError(Process process) {
		BufferedReader stderr = new BufferedReader(new InputStreamReader(
			process.getErrorStream()));
		BufferedReader stdout = new BufferedReader(new InputStreamReader(
			process.getInputStream()));
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		StringBuffer error = new StringBuffer();
		try {
			while (stderr.ready()) {
				error.append(stderr.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		StringBuffer output = new StringBuffer();
		try {
			while (stdout.ready()) {
				String line = stdout.readLine();
				if (line.toLowerCase().indexOf("failed") > -1)
					error.append(line);
				output.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return error.toString().trim();
	}
	
	public static List<String> waitToTerminateandGetOutput(Process process)
		throws IOException {
		BufferedReader stderr = new BufferedReader(new InputStreamReader(
			process.getErrorStream()));
		BufferedReader stdout = new BufferedReader(new InputStreamReader(
			process.getInputStream()));
		try {
			process.waitFor();
		} catch (InterruptedException e) {
		}
		StringBuffer error = new StringBuffer();
		while (stderr.ready()) {
			error.append(stderr.readLine());
		}
		if (!"".equals(error.toString().trim()))
			throw new IOException(error.toString());
		
		ArrayList<String> lines = new ArrayList<String>();
		while (stdout.ready()) {
			lines.add(stdout.readLine());
		}
		return lines;
	}
	
	public static void tryToParseMappingAndAddTo(String line,
		List<PortMapping> result) {
		PortMapping p;
		try {
			p = parse(line);
			if (p != null)
				result.add(p);
		} catch (Exception e) {
		}
	}
	
	public static PortMapping parse(String line) throws Exception {
		line = line.trim();
		String r_ip = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		String regex = "\\d+ (UDP|TCP) (\\d{1,5})->(" + r_ip
			+ "):(\\d{1,5}) '(.*)' '(.*)' (\\d+)";
		Matcher m = Pattern.compile(regex).matcher(line);
		if (!m.matches())
			throw new Exception("Line does not match the expected pattern.");
		
		String protocol = m.group(1);
		int externPort = Integer.valueOf(m.group(2));
		String ip = m.group(3);
		int localPort = Integer.valueOf(m.group(4));
		String description = m.group(5);
		String remoteHost = m.group(6);
		int lifetime = Integer.valueOf(m.group(7));
		return new PortMapping(protocol, ip, externPort, localPort,
			description, remoteHost, lifetime);
	}
	
}
