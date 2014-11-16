package com.ecse414.meshsim.router;

public class RouteTableEntry {
	
	private String name;
	private int hops;
	private String nextHopIp;
	private int nextHopPort;
	//private Time lifetime;
	
	public RouteTableEntry(String name, int hops, String nextHopIp,
			int nextHopPort) {
		super();
		this.name = name;
		this.hops = hops;
		this.nextHopIp = nextHopIp;
		this.nextHopPort = nextHopPort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public String getNextHopIp() {
		return nextHopIp;
	}

	public void setNextHopIp(String nextHopIp) {
		this.nextHopIp = nextHopIp;
	}

	public int getNextHopPort() {
		return nextHopPort;
	}

	public void setNextHopPort(int nextHopPort) {
		this.nextHopPort = nextHopPort;
	}
	
	public boolean isValid() {
		return true;
	}
	
}
