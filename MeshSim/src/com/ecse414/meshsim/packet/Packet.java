package com.ecse414.meshsim.packet;

public interface Packet {
	
	public byte getType();
	
	public byte[] toBytes();
	
	public void parseBytes(byte[] bytes);
	
	public String getDestinationAddress();
	
}
