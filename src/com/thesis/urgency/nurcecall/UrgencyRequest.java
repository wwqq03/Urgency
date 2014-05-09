package com.thesis.urgency.nurcecall;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class UrgencyRequest {
	
	private String callId;
	private String patient;
	
	public UrgencyRequest(String rawMessage) {
		if(rawMessage == null) {
			return;
		}
		
		try {
			
			Document document = DocumentHelper.parseText(rawMessage);
			Element requestElement = document.getRootElement();
			
			if(requestElement == null) {
				return;
			}
			
			callId = requestElement.elementText("callId");
			patient = requestElement.elementText("patient");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCallId() {
		return callId;
	}

	public String getPatient() {
		return patient;
	}
}
