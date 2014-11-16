package com.ecse414.meshsim.packet;

import java.nio.charset.Charset;

import com.ecse414.meshsim.main.Constants;

/**
 * Represents a data packet that contains a string msg
 * @author mverte
 */
public class DataPacket implements Packet {

	/**
	 * 1 byte PKT_TYPE
	 * 1 byte hopCount
	 * x byte sourceAddr
	 * x byte destAddr
	 * x byte data
	 */
	private byte hopCount;
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
		return "" + this.getType() + Constants.BRK +
			   sourceAddr + Constants.BRK +
			   destAddr + Constants.BRK +
			   data; // + Constants.BRK;
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
		String[] s = new String(bytes).split(String.valueOf(Constants.BRK), 4);
		if (s.length != 4) {
			System.err.println("Could not parse data packet!");
		} 
		// s[0] is the packet type
		this.sourceAddr = s[1];
		this.destAddr = s[2];
		this.data = s[3];
		System.out.println("DATAFROMPKT:"+this.data);
	}

	@Override
	public String getDestinationAddress() {
		return destAddr;
	}
	
	@Override
	public byte getHopCount() {
		return hopCount;
	}

	@Override
	public void incrementHopCount() {
		hopCount++;		
	}
	
	public String getSourceAddress() {
		return sourceAddr;
	}
	
	public String getData() {
		return data;
	}

}