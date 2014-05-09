package com.thesis.urgency.nurcecall;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class UrgencyResponse {

	private String status;
	private String message;
	private String urgency;
	
	public UrgencyResponse(String status) {
		// TODO Auto-generated constructor stub
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
	public String toXML() {
		String xml = null;
		
		try{
			Document document = DocumentHelper.createDocument();
            Element responseElement = document.addElement("response");
            responseElement.addAttribute("status", status);
            
            if(status.equals("200")){
            	if(urgency != null && !urgency.isEmpty()) {
            		Element urgencyElement = responseElement.addElement("urgency");
            		urgencyElement.setText(urgency);
            	}
            }
            else if(message != null) {
            	Element messageElement = responseElement.addElement("message");
            	messageElement.setText(message);
            }
            
            xml = responseElement.asXML();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return xml;
	}
}
