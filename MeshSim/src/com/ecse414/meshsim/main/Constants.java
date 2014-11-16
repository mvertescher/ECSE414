package com.ecse414.meshsim.main;

public class Constants {

	/**
	 * IP for the localhost machine, seen from an AVD
	 */
	public static final String LOCALHOST = "10.0.2.2";
	
	/**
	 * Port that all the AVDs listen on
	 */
	public static final int RECV_PORT = 5000;
	
	/**
	 * Break character used to separate data fields
	 */
	public static final char BRK = '\n';//0x0A;
	
	/**
	 * Packet containing user data
	 */
	public static final byte PKT_DATA = 1;
	
	/**
	 * Hello packet
	 */
	public static final byte PKT_HELLO = 2;
	
	/**
	 * A route reply packet 
	 */
	public static final byte PKT_RREQ = 3;
	
	/**
	 * A route request packet 
	 */
	public static final byte PKT_RREP = 4;
	
	/**
	 * Hello packet timeout in ms
	 */
	public static final int HELLO_TIMEOUT = 10000;
}
