package com.ecse414.meshsim.main;

import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.ecse414.meshsim.packet.HelloPacket;
import com.ecse414.meshsim.packet.Packet;
import com.ecse414.meshsim.router.RouteTable;
import com.ecse414.meshsim.tcp.TcpSender;

/**
 * Responsible for sending all packets that appear in the queue
 * @author mverte
 */
public class Sender implements Runnable {
	
	private MeshNode root; 
	private RouteTable routeTable;
	private ConcurrentLinkedQueue<Packet> ccl;
	
	public Sender(MeshNode root, RouteTable routeTable) {
		this.root = root;
		this.routeTable = routeTable;
		this.ccl = new ConcurrentLinkedQueue<Packet>();
	}
	
	public boolean queuePacket(Packet p) {
		return this.ccl.add(p);
	}
	
	@Override
	public void run() {
		TcpSender packetSender = new TcpSender();
		// Begin queuing hello packets to be sent
		//this.enqueueHelloPackets();
		while (true) {
			
			// A spin wait is super resource heavy
			// TODO: not this
			while (this.ccl.isEmpty()) {
				Thread.yield();
			}
			
			Packet p = this.ccl.remove();
			
			String[] ipPort = p.getDestinationAddress().split(":", 2);
			String ip = ipPort[0];
			int port = Integer.parseInt(ipPort[1]);
			
			
			
			packetSender.sendPacket(ip, port, p.toBytes());
			
			//packetSender.sendTcpPacket(p);
			// Need to check if a route exists
			
			//new Thread(new PacketSender(p)).start();
		}
	}
	
	private void enqueueHelloPackets() {
	    Timer timer = new Timer();
	    TimerTask timeTask = new TimerTask() {       
	        @Override
	        public void run() {	
	        	byte hopCount = 0;
	        	String sourceAddr = root.getIpPort();
	        	byte numNeighbors = (byte) root.getNeighbors().size();
	        	String[] neighborAddr = new String[numNeighbors];
	        	String[] neighborName = new String[numNeighbors];
	        	int i = 0;
	        	for (Entry<String, MeshNode> e : root.getNeighbors().entrySet()) {
	        		neighborAddr[i] = e.getValue().getIpPort(); 
	        		neighborName[i] = e.getKey();
	        		i++;
	        	}
	        	
	        	for (Entry<String, MeshNode> e : root.getNeighbors().entrySet()) {
	        		ccl.add(new HelloPacket(hopCount, 
	        				sourceAddr, 
	    	        		e.getValue().getIpPort(), 
	    	        		numNeighbors, 
	    	        		neighborAddr, 
	    	        		neighborName));	
	        	}
	        }
	    };
	    timer.schedule(timeTask, 3000, Constants.HELLO_TIMEOUT);
	}

}
