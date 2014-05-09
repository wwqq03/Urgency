package com.thesis.urgency;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class UrgencyRequestReceiver implements Runnable{
	private BlockingQueue<Socket> queue;
	
	public UrgencyRequestReceiver(BlockingQueue<Socket> queue) {
		this.queue = queue;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(Integer.valueOf(Server.URGENCY_PORT));
			System.out.println("Urgency request receiver started on port " + Server.URGENCY_PORT);
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				queue.put(clientSocket);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
