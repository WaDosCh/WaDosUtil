package ch.judos.generic.network.udp.model;

import java.io.Serializable;

/**
 * sent over the network to receiver for information
 * 
 * @since 12.07.2013
 * @author Julian Schelker
 */
public class FileDescription implements Serializable {

	private static final long serialVersionUID = -1694298991434697351L;
	private String description;
	private long length;

	private int parts;

	private String path;

	public FileDescription(long length, int parts, String path, String description) {
		this.length = length;
		this.parts = parts;
		this.path = path;
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return this.length;
	}

	/**
	 * @return the parts
	 */
	public int getParts() {
		return this.parts;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return this.path;
	}

}
