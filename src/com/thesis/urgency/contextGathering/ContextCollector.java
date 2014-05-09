package com.thesis.urgency.contextGathering;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.thesis.urgency.Server;
import com.thesis.urgency.common.ContextItem;

public class ContextCollector {
	private Socket socket;
	
	public ArrayList<ContextItem> getContext(String subject) {
		
		try{
			socket = new Socket(Server.CONTEXT_GATHERING_IP, Integer.valueOf(Server.CONTEXT_GATHERING_PORT));
			
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			
			Request request = new Request(subject);
			
			String msgToSend = request.toXML();
			if(msgToSend == null || msgToSend.isEmpty()) {
				return null;
			}
			writer.println(msgToSend);
			writer.flush();
			
			String rawResponse = reader.readLine();
			reader.close();
			
			Response response = new Response(rawResponse);
			if(response.isSuccess()){
				return response.getItems();
			} else {
				System.out.println("Request for " + subject + "'s context failed, reason: [" + response.getMessage() + "]");
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try{
				socket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
