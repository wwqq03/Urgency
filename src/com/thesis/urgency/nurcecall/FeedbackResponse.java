package com.thesis.urgency.nurcecall;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class FeedbackResponse {
	private String status;
	private String message;
	
	public FeedbackResponse(String status) {
		// TODO Auto-generated constructor stub
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toXML() {
		String xml = null;
		
		try{
			Document document = DocumentHelper.createDocument();
            Element responseElement = document.addElement("response");
            responseElement.addAttribute("status", status);
            
            if(status.equals("200")){
            	
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
