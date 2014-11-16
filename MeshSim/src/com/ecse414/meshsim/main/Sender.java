package com.ecse414.meshsim.main;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ecse414.meshsim.packet.Packet;
import com.ecse414.meshsim.tcp.TcpSender;

/**
 * Responsible for sending all packets that appear in the queue
 * @author mverte
 */
public class Sender implements Runnable {
	
	private MeshNode root; 
	private ConcurrentLinkedQueue<Packet> ccl;
	
	public Sender(MeshNode root) {
		this.root = root;
		this.ccl = new ConcurrentLinkedQueue<Packet>();
	}
	
	public boolean queuePacket(Packet p) {
		return this.ccl.add(p);
	}
	
	@Override
	public void run() {
		TcpSender packetSender = new TcpSender();
		while (true) {
			
			// A spin wait is super resource heavy
			// TODO: not this
			while (ccl.isEmpty());			
			
			Packet p = ccl.remove();
			
			String[] ipPort = p.getDestinationAddress().split(":", 2);
			String ip = ipPort[0];
			int port = Integer.parseInt(ipPort[1]);
			
			packetSender.sendPacket(ip, port, p.toBytes());
			
			//packetSender.sendTcpPacket(p);
			// Need to check if a route exists
			
			//new Thread(new PacketSender(p)).start();
		}
	}

}
