package com.ecse414.meshsim.main;

import java.util.ArrayList;
import java.util.List;

public class MeshNode {
	private final String id;
	private final String name;
	private final int port;
	private List<MeshNode> neighbors;
	
	public MeshNode(String id, String name, int port) {
		this.id = id;
		this.name = name;
		this.port = port;
		this.neighbors = new ArrayList<MeshNode>();
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
	
	public List<MeshNode> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<MeshNode> neighbors) {
		this.neighbors = neighbors;
	}
	
}
