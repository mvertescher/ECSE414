package com.ecse414.meshsim.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TcpReciever implements Runnable {
	
	private ServerSocket serverSocket;
	private ConcurrentLinkedQueue<Socket> socketQueue;
	
	public TcpReciever(int port, ConcurrentLinkedQueue<Socket> queue) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Server socket on port " + port + " could not be created. ");
			e.printStackTrace();
		}
		this.socketQueue = queue;
	}

	@Override
	public void run() {
		Socket socket;
		while (!Thread.currentThread().isInterrupted()) {		
			try {
				socket = this.serverSocket.accept();
				System.err.println("Server | connection accepted, added to socket queue");
				this.socketQueue.add(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}