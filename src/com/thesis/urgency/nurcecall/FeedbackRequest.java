package com.thesis.urgency.nurcecall;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class FeedbackRequest {
	
	private String callId;
	private String urgency;

	public FeedbackRequest(String rawMessage) {
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
			urgency = requestElement.elementText("urgency");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCallId() {
		return callId;
	}

	public String getUrgency() {
		return urgency;
	}
}
