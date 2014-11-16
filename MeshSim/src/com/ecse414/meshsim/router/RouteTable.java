package com.ecse414.meshsim.router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RouteTable {
	
	private Map<String,RouteTableEntry> table;
	
	public RouteTable() {
		table = new ConcurrentHashMap<String,RouteTableEntry>();
	}
	
	public int getRoute(String id) {
		RouteTableEntry entry = table.get(id);

		if (entry != null) {
			if (entry.isValid()) {
				return entry.getNextHopPort();
			}
		}
		
		return 0;
	}
	
	public boolean addRoute(String id, String name, int hops, String nextHopIp, int nextHopPort) {
		RouteTableEntry entry = new RouteTableEntry(name,hops,nextHopIp,nextHopPort);
		table.put(id, entry);
		return true;
	}
	
	
}
