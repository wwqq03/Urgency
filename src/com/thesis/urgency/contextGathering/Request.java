package com.thesis.urgency.contextGathering;

import java.awt.event.ItemEvent;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Request {
	private String subject;
	
	public Request(String subject) {
		this.subject = subject;
	}
	
	public String toXML() {
		String xml = null;
		try {
			Document document = DocumentHelper.createDocument();
	        Element requestElement = document.addElement("request");
	        
	        Element itemElement = requestElement.addElement("item");
	        itemElement.addAttribute("subject", subject);
	        xml = requestElement.asXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
}
