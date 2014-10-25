package com.ecse414.meshsim.main;

public class Constants {

	/**
	 * IP for the localhost machine, seen from an AVD
	 */
	public static final String LOCALHOST = "10.0.2.2";
	
	/**
	 * Break character used to separate data fields
	 */
	public static final char BRK = '\n';//0x0A;
	
	/**
	 * Packet containing user data
	 */
	public static final byte PKT_DATA = 1;
	
	/**
	 * A route reply packet 
	 */
	public static final byte PKT_RREQ = 2;
	
	/**
	 * A route request packet 
	 */
	public static final byte PKT_RREP = 3;
	
}
