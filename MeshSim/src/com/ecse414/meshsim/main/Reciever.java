package com.ecse414.meshsim.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ecse414.meshsim.tcp.TcpReciever;

import android.os.Handler;
import android.widget.TextView;

public class Reciever implements Runnable {

	private MeshNode root;
	//private ServerSocket serverSocket;

	Handler updateConversationHandler;
	TextView textView;
	
	
	public Reciever(MeshNode root, Handler handler, TextView textView) {
		this.root = root;
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
		
		String[] str1 = fullStr.split(String.valueOf(Constants.BRK), 2);
		String pkt_line = str1[0];
		
		if (pkt_line == null) {
			System.err.println("PacketHandlerThread | pkt_line == null.");
		}
			
		byte pkt_type = Byte.parseByte(pkt_line);
			
		// Determine the type of packet received
		switch (pkt_type) {
			case (Constants.PKT_DATA):
				handleDataPacket(str1[1]);
				break; 
			default: 
				System.err.println("PacketHandlerThread | Invalid pkt_type.");
				break; 
		}
		
	}
	
	private void handleDataPacket(String str) {
		str = str.split(String.valueOf(Constants.BRK), 2)[1]; // cutting srcAddr
		String data = str.split(String.valueOf(Constants.BRK), 2)[1]; // cutting destAddr
		updateConversationHandler.post(new updateUIThread("", data));
	}
	

	
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