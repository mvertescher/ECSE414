package com.ecse414.meshsim.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ecse414.meshsim.packet.DataPacket;
import com.ecse414.meshsim.packet.HelloPacket;
import com.ecse414.meshsim.router.RouteTable;
import com.ecse414.meshsim.tcp.TcpReciever;

import android.os.Handler;
import android.widget.TextView;

public class Reciever implements Runnable {

	private MeshNode root;
	private RouteTable routeTable;
	private Sender sender;

	Handler updateConversationHandler;
	TextView textView;
	
	public Reciever(MeshNode root, RouteTable routeTable, Sender sender, Handler handler, TextView textView) {
		this.root = root;
		this.routeTable = routeTable;
		this.sender = sender; 
		this.updateConversationHandler = handler;
		this.textView = textView;
	}
	
	public void run() {
		ConcurrentLinkedQueue<Socket> socketQueue = new ConcurrentLinkedQueue<Socket>();
		
		// Start the TcpReciver 
		new Thread(new TcpReciever(root.getPort(), socketQueue)).start();
		
		byte[] bytes = new byte[1024];
		Socket socket;
		
		while (true) {
			while (socketQueue.isEmpty()) {
				Thread.yield();
			}
			
			socket = socketQueue.remove();
			try {
				InputStream in = socket.getInputStream();
				in.read(bytes);				
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.processBytes(bytes);		
		}
	}
	
	private void processBytes(byte[] bytes) {
		String fullStr =  new String(bytes);
		System.err.println("PacketHandlerThread | fullStr=" + fullStr);	
		
		//String[] str1 = fullStr.split(String.valueOf(Constants.BRK), 2);
		//String pkt_line = str1[0];
		//String pkt_data = str1[1];
		
		
		//if (pkt_line == null) {
		//	System.err.println("PacketHandlerThread | pkt_line == null.");
		//}
			
		//byte pkt_type = Byte.parseByte(pkt_line);
			
		byte pkt_type = (byte) (bytes[0] - 48);
		System.out.println("Pkt type =(" + pkt_type + ")");
		
		// Determine the type of packet received
		switch (pkt_type) {
			case (Constants.PKT_HELLO):
				handleHelloPacket(new HelloPacket(bytes));
				break;
			case (Constants.PKT_DATA):
				handleDataPacket(new DataPacket(bytes));
				break; 
			default: 
				System.err.println("PacketHandlerThread | Invalid pkt_type.");
				break; 
		}
		
	}
	
	private void handleHelloPacket(HelloPacket helloPacket) {
		// Received a hello packet -> update routing table
		updateConversationHandler.post(new updateUIThread("", "HPKT RECV from " + helloPacket.getSourceAddress()));
	}

	private void handleDataPacket(DataPacket dataPacket) {
		// If the packet is not meant for this node, forward
		if ((Constants.LOCALHOST + ":" + root.getPort()).equals(dataPacket.getDestinationAddress())) {
			this.sender.queuePacket(new DataPacket(dataPacket.getSourceAddress(), 
					dataPacket.getDestinationAddress(), 
					dataPacket.getData()));
		} else {
			updateConversationHandler.post(new updateUIThread("", dataPacket.getData()));
		}
	}

	/*private void handleDataPacket(String str) {
		String[] srcSplit = str.split(String.valueOf(Constants.BRK), 2); // cutting srcAddr
		String srcAddr = srcSplit[0];
		String[] destSplit = srcSplit[1].split(String.valueOf(Constants.BRK), 2); // cutting destAddr
		String destAddr = destSplit[0];
		String data = destSplit[1];
		
		// If the packet is not meant for this node, forward
		if ((Constants.LOCALHOST + ":" + root.getPort()).equals(destAddr)) {
			this.sender.queuePacket(new DataPacket(srcAddr,destAddr,data));
		} else {
			updateConversationHandler.post(new updateUIThread("", data));
		}
	}*/
	

	
	class updateUIThread implements Runnable {
		private String sender;
		private String msg;

		public updateUIThread(String sender, String str) {
			this.sender = sender;
			this.msg = str;
		}

		@Override
		public void run() {
			textView.setText(textView.getText().toString() + msg + "\n");
		}
	}

}