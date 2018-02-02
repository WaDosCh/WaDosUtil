package controller.portforwarding;

/**
 * @author Julian Schelker
 * @lastupdate 23.04.2012
 */
public class PortMapping {
	
	protected String protocol;
	protected String ip;
	protected int externPort;
	protected int localPort;
	protected String description;
	protected String remoteHost;
	protected int lifeTime;
	
	public String getProtocol() {
		return protocol;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getExternPort() {
		return externPort;
	}
	
	public int getLocalPort() {
		return localPort;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getRemoteHost() {
		return remoteHost;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
	public PortMapping(String protocol, String ip, int externPort,
		int localPort, String description, String remoteHost, int lifetime) {
		this.protocol = protocol;
		this.ip = ip;
		this.externPort = externPort;
		this.localPort = localPort;
		this.description = description;
		this.remoteHost = remoteHost;
		this.lifeTime = lifetime;
	}
	
	public String toString() {
		return protocol + " " + externPort + " " + ip + ":" + localPort + " "
			+ description + " " + remoteHost + " " + lifeTime;
	}
}
