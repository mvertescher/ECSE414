package com.ecse414.meshsim.packet;

import java.nio.charset.Charset;

import com.ecse414.meshsim.main.Constants;

/**
 * Represents a hello packet to make 
 * nodes aware of each other
 * @author Matt
 */
public class HelloPacket implements Packet {
	
	/**
	 * 1 byte PKT_TYPE
	 * 1 byte hopCount
	 * x byte sourceAddr
	 * x byte destAddr
	 * 1 byte num_neighbors
	 * for num_neighbors:
	 *		32 byte neighborAddr
	 */
	private byte hopCount;
	private String sourceAddr;
	private String destAddr;
	private byte numNeighbors;
	private String[] neighborAddr;
	private String[] neighborName;
	
	public HelloPacket(byte hopCount, String sourceAddr, String destAddr,
			byte numNeighbors, String[] neighborAddr, String[] neighborName) {
		this.hopCount = hopCount;
		this.sourceAddr = sourceAddr;
		this.destAddr = destAddr;
		this.numNeighbors = numNeighbors;
		this.neighborAddr = neighborAddr;
		this.neighborName = neighborName;
	}
	
	public HelloPacket(byte[] bytes) {
		this.parseBytes(bytes);
	}

	@Override
	public String toString() {
		String fixed = "" + this.getType() + Constants.BRK +
					   this.hopCount + Constants.BRK +
					   this.sourceAddr + Constants.BRK + 
					   this.destAddr + Constants.BRK +
					   this.numNeighbors + Constants.BRK;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.numNeighbors; i++) {
			sb.append(this.neighborAddr[i] + Constants.BRK);
			sb.append(this.neighborName[i] + Constants.BRK);
		}
		/*StringBuilder sb = new StringBuilder();
		sb.append(this.getType()); sb.append(Constants.BRK);
		sb.append((char) this.hopCount);  sb.append(Constants.BRK);
		sb.append(this.sourceAddr + Constants.BRK);
		sb.append(this.destAddr + Constants.BRK);
		sb.append((char) this.numNeighbors); sb.append(Constants.BRK);
		for (int i = 0; i < this.numNeighbors; i++) {
			sb.append(this.neighborAddr[i] + Constants.BRK);
			sb.append(this.neighborName[i] + Constants.BRK);
		}
		System.out.println(sb.toString().getBytes(Charset.forName("UTF-8")));*/
		return fixed + sb.toString();
	}
	
	@Override
	public byte getType() {
		return Constants.PKT_HELLO;
	}

	@Override
	public byte[] toBytes() {
		return this.toString().getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public void parseBytes(byte[] bytes) {
		String[] s = new String(bytes).split(String.valueOf(Constants.BRK), 6);
		if (s.length != 6) {
			System.err.println("Could not parse hello packet!");
		}
		// s[0] is the packet type
		this.hopCount = Byte.valueOf(s[1]);
		this.sourceAddr = s[2];
		this.destAddr = s[3];
		this.numNeighbors = Byte.valueOf(s[4]);
		this.neighborAddr = new String[this.numNeighbors];
		this.neighborName = new String[this.numNeighbors];
		
		if (this.numNeighbors > 0) {
			int limit = ((int) this.numNeighbors) * 2;
			int slowIndex = 0;
			String[] n = s[5].split(String.valueOf(Constants.BRK), limit);
			for (int i = 0; i < limit; i = i + 2) {
				this.neighborAddr[slowIndex] = n[i];
				this.neighborName[slowIndex] = n[i + 1];
				slowIndex++;
			}
		} else {
			this.neighborAddr = null;
			this.neighborName = null;
		}
	}

	@Override
	public String getDestinationAddress() {
		return this.destAddr;
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
		return this.sourceAddr;
	}

}
