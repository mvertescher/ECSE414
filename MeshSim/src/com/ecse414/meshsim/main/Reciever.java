package com.ecse414.meshsim.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.widget.TextView;

public class Reciever implements Runnable {

	private MeshNode root;
	private ServerSocket serverSocket;

	Handler updateConversationHandler;
	TextView textView;
	
	
	public Reciever(MeshNode root, Handler handler, TextView textView) {
		this.root = root;
		this.updateConversationHandler = handler;
		this.textView = textView;
	}
	
	public void run() {
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(root.getPort());
		} catch (IOException e) {
			System.err.println("Server socket on port " + root.getPort() + " could not be created. ");
			e.printStackTrace();
		}
		while (!Thread.currentThread().isInterrupted()) {		
			try {
				socket = serverSocket.accept();
				System.err.println("Server | connection accepted, handling communication");
				ConnectionHandlerThread connThread = new ConnectionHandlerThread(socket);
				new Thread(connThread).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ConnectionHandlerThread implements Runnable {

		private Socket clientSocket;
		//private BufferedReader input;
		private byte[] bytes = new byte[1024];
		
		
		public ConnectionHandlerThread(Socket clientSocket) {
			this.clientSocket = clientSocket;
			System.out.println("Handling packet bound to local port " + this.clientSocket.getLocalPort());
			try {
				InputStream in = this.clientSocket.getInputStream();
				in.read(bytes);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
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