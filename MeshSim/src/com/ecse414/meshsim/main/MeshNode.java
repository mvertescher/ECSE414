package com.ecse414.meshsim.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeshNode {
	private final String id;
	private final String name;
	private final int port;
	private final String ip;
	
	private HashMap<String, MeshNode> neighbors;
	
	public MeshNode(String id, String name, int port) {
		this.ip = Constants.LOCALHOST;
		this.id = id;
		this.name = name;
		this.port = port;
		this.neighbors = new HashMap<String, MeshNode>();
	}
	
	public MeshNode(String id, String name, String ipport) {
		this.id = id;
		this.name = name;
		String[] ipports = ipport.split(":");
		this.ip = ipports[0];
		this.port = Integer.parseInt(ipports[1]);
		this.neighbors = new HashMap<String, MeshNode>();
	}

	public String getIp(){
		return ip;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getIpPort() {
		return this.ip + ":" + this.port;
	}
	
	public HashMap<String, MeshNode> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(HashMap<String, MeshNode> neighbors) {
		this.neighbors = neighbors;
	}
	
}
