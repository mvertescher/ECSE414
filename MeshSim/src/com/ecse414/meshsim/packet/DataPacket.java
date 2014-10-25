package com.ecse414.meshsim.packet;

import java.nio.charset.Charset;

import com.ecse414.meshsim.main.Constants;

public class DataPacket implements Packet {

	private String sourceAddr;
	private String destAddr;
	private String data;
	
	public DataPacket(byte[] bytes) {
		this.parseBytes(bytes);
	}
	
	public DataPacket(String sourceAddr, String destAddr, String data) {
		this.sourceAddr = sourceAddr;
		this.destAddr = destAddr;
		this.data = data;
	}
	
	@Override 
	public String toString() {
		return this.getType() + Constants.BRK +
				sourceAddr + Constants.BRK +
				destAddr + Constants.BRK +
				data;
	}
	
	@Override
	public byte getType() {
		return Constants.PKT_DATA;
	}

	@Override
	public byte[] toBytes() {
		return this.toString().getBytes(Charset.forName("UTF-8"));
	}
	
	@Override
	public void parseBytes(byte[] bytes) {
		String[] s = new String(bytes).split("\n", 3);
		if (s.length != 3) {
			System.err.println("Could not parse data packet!");
		} 
		this.sourceAddr = s[0];
		this.destAddr = s[1];
		this.data = s[2];
	}

	@Override
	public String getDestinationAddress() {
		return destAddr;
	}

}