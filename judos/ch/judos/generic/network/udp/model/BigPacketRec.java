package ch.judos.generic.network.udp.model;

import static ch.judos.generic.network.udp.model.BigPacketSend.CONTROL_DATA_BYTES;

import java.net.InetSocketAddress;

import ch.judos.generic.data.Serializer;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class BigPacketRec {

	public static IAddressAndId getHashObject(byte[] packetData, InetSocketAddress from) {
		short id = Serializer.bytes2short(packetData, 0);
		return new IAddressAndId(id, from);
	}

	private byte[][] data;
	private short id;
	private int missing;
	private short parts;

	public BigPacketRec() {
		this.parts = 0;
	}

	public int addPart(byte[] packetData) {
		short index = Serializer.bytes2short(packetData, 2);
		if (this.parts == 0) {
			this.id = Serializer.bytes2short(packetData, 0);
			this.parts = Serializer.bytes2short(packetData, 4);
			this.data = new byte[this.parts][];
			this.missing = this.parts;
		}

		// remove the control data
		byte[] part = new byte[packetData.length - CONTROL_DATA_BYTES];
		System.arraycopy(packetData, CONTROL_DATA_BYTES, part, 0, part.length);
		if (this.data[index] == null)
			this.missing--;
		this.data[index] = part;
		return index;
	}

	public byte[] getData() {
		int totalSizeBytes = 0;
		for (byte[] part : this.data)
			totalSizeBytes += part.length;
		byte[] all = new byte[totalSizeBytes];
		int index = 0;
		for (byte[] part : this.data) {
			System.arraycopy(part, 0, all, index, part.length);
			index += part.length;
		}
		return all;
	}

	public short getId() {
		return this.id;
	}

	public int getParts() {
		return this.parts;
	}

	public boolean isFinished() {
		return this.missing == 0;
	}

}
