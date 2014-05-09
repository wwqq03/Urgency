package com.thesis.urgency;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import com.thesis.urgency.DAO.TempContextDAO;
import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;
import com.thesis.urgency.common.DualPatientCase;
import com.thesis.urgency.common.MyCBRPatientCase;
import com.thesis.urgency.nurcecall.FeedbackRequest;
import com.thesis.urgency.nurcecall.FeedbackResponse;
import com.thesis.urgency.persistentStore.DualPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.MyCBRPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.PersistentStore;

public class Adjustor implements Runnable{
	private BlockingQueue<Socket> queue;
	private PersistentStore patientCasesPersistentStore;
	private TempContextDAO tempContextDAO;
	
	public Adjustor(PersistentStore persistentStore, BlockingQueue<Socket> queue) {
		patientCasesPersistentStore = persistentStore;
		this.queue = queue;
		tempContextDAO = new TempContextDAO();
	}
	
	public void run() {
		while(true) {
			try {
				Socket clientSocket = queue.take();
				String message = getRawMessage(clientSocket);
				FeedbackResponse response = processMessage(message);
				
				if(response == null) {
					response = new FeedbackResponse("400");
					response.setMessage("Bad request");
				}
				
				sendResponse(clientSocket, response);
				
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendResponse(Socket clientSocket, FeedbackResponse response) {
		// TODO Auto-generated method stub
		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			writer.println(response.toXML());
			writer.close();
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private FeedbackResponse processMessage(String message) {
		// TODO Auto-generated method stub
		if(message == null || message.isEmpty()) {
			return null;
		}
		
		FeedbackRequest request = new FeedbackRequest(message);
		String urgency = request.getUrgency();
		String callId = request.getCallId();
		
		storeNewCase(callId, urgency);
		
		FeedbackResponse response = new FeedbackResponse("200");
		return response;
	}

	private void storeNewCase(String callId, String urgency) {
		// TODO Auto-generated method stub
		ArrayList<ContextItem> context = tempContextDAO.read(callId);
		Case newCase;
		if(patientCasesPersistentStore instanceof DualPatientCasesPersistentStore) {
			newCase = new DualPatientCase();
			newCase.setUrgency(urgency);
			newCase.setContext(context);
		} else if(patientCasesPersistentStore instanceof MyCBRPatientCasesPersistentStore) {
			newCase = new MyCBRPatientCase();
			newCase.setUrgency(urgency);
			newCase.setContext(context);
		} else {
			return;
		}
		patientCasesPersistentStore.addCase(newCase);
	}

	private String getRawMessage(Socket clientSocket) {
		// TODO Auto-generated method stub
		try {
			InputStreamReader streamReader = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);			
			String message = reader.readLine();
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
