package com.thesis.urgency;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import com.thesis.urgency.DAO.TempContextDAO;
import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;
import com.thesis.urgency.contextGathering.ContextCollector;
import com.thesis.urgency.nurcecall.UrgencyRequest;
import com.thesis.urgency.nurcecall.UrgencyResponse;
import com.thesis.urgency.persistentStore.DualPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.MyCBRPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.PersistentStore;

public class Analyzer implements Runnable{
	
	private PersistentStore patientCasesPersistentStore;
	private ContextCollector collector;
	private TempContextDAO tempContextDAO = new TempContextDAO();
	private BlockingQueue<Socket> queue;
	
	public Analyzer(PersistentStore persistentStore, BlockingQueue<Socket> queue) {
		this.queue = queue;
		patientCasesPersistentStore = persistentStore;
		collector = new ContextCollector();
	}
	
	public void run() {
		while(true) {
			try {
				Socket clientSocket = queue.take();
				String message = getRawMessage(clientSocket);
				UrgencyResponse response = processMessage(message);
				
				if(response == null){
					response = new UrgencyResponse("400");
					response.setMessage("Bad request!");
				}
				
				sendResponse(clientSocket, response);
				
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendResponse(Socket clientSocket, UrgencyResponse response) {
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

	private UrgencyResponse processMessage(String message) {
		// TODO Auto-generated method stub
		if(message == null || message.isEmpty()) {
			return null;
		}
		
		UrgencyRequest request = new UrgencyRequest(message);
		String patient = request.getPatient();
		String callId = request.getCallId();
		
		String urgency = getUrgency(patient, callId);
		UrgencyResponse response = new UrgencyResponse("200");
		response.setUrgency(urgency);
		
		return response;
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

	public String getUrgency(String subject, String callId) {
		if(patientCasesPersistentStore == null) {
			return "0";
		}
		
		ArrayList<ContextItem> subjectContext = collector.getContext(subject);
		Case chosenCase = patientCasesPersistentStore.getMostSimilarCase(subjectContext);
		if(chosenCase == null) {
			return "0";
		}
		
		storeNewContext(callId, subjectContext);
		
		return chosenCase.getUrgency();
	}

	private void storeNewContext(String callId, ArrayList<ContextItem> newContext) {
		// TODO Auto-generated method stub
		tempContextDAO.store(callId, newContext);
	}
}
