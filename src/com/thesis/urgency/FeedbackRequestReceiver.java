package com.thesis.urgency;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class FeedbackRequestReceiver implements Runnable{
	private BlockingQueue<Socket> queue;
	
	public FeedbackRequestReceiver(BlockingQueue<Socket> queue) {
		this.queue = queue;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(Integer.valueOf(Server.FEEDBACK_PORT));
			System.out.println("Feedback request receiver started on port " + Server.FEEDBACK_PORT);
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				queue.put(clientSocket);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
