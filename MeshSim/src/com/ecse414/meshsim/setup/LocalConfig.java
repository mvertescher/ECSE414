package com.ecse414.meshsim.setup;

import com.ecse414.meshsim.main.MeshNode;

public class LocalConfig {

	private LocalConfig() {
	}
	
	/**
	 * Enables the AVD to determine who it is, what ports to use
	 * and who its neighbors are.
	 * @param androidId
	 * @return a MeshNode representing the root
	 */
	public static MeshNode getRoot(String androidId) {
		
		MeshNode rootNode = null;
		MeshNode node1 = new MeshNode(AVDConstants.AVD1_ID, "AVD1", AVDConstants.AVD1_SERVER_PORT);
		MeshNode node2 = new MeshNode(AVDConstants.AVD2_ID, "AVD2", AVDConstants.AVD2_SERVER_PORT);
		MeshNode node3 = new MeshNode(AVDConstants.AVD3_ID, "AVD3", AVDConstants.AVD3_SERVER_PORT);
		
		node1.getNeighbors().add(node2);
		node2.getNeighbors().add(node1);
		node2.getNeighbors().add(node3);
		node3.getNeighbors().add(node2);
		
		if (AVDConstants.AVD1_ID.equals(androidId)) {
			rootNode = node1;
		} else if (AVDConstants.AVD2_ID.equals(androidId)) {
			rootNode = node2;
		} else if (AVDConstants.AVD3_ID.equals(androidId)) {
			rootNode = node3;
		} 
		
		return rootNode;
	}
	
}
