package com.thesis.urgency.contextGathering;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thesis.urgency.common.ContextItem;

public class Response {
	private String status;
	private ArrayList<ContextItem> items = new ArrayList<ContextItem>();
	private String message;
	
	public String getStatus() {
		return status;
	}

	public ArrayList<ContextItem> getItems() {
		return items;
	}

	public String getMessage() {
		return message;
	}

	public Response(String rawMessage) {
		if(rawMessage == null || rawMessage.isEmpty()) {
			return;
		}
		
		try {
			Document document = DocumentHelper.parseText(rawMessage);
			Element responseElement = document.getRootElement();
			
			status = responseElement.attributeValue("status");
			if(status == null) {
				return;
			}
			if(status.equals("200")) {
				ArrayList<Element> itemElements = new ArrayList<Element>(responseElement.elements("item"));
				Iterator<Element> i = itemElements.iterator();
				while(i.hasNext()) {
					Element itemElement = i.next();
					String subject = itemElement.attributeValue("subject");
					String predicate = itemElement.elementText("predicate");
					String object = itemElement.elementText("object");
					
					if(predicate != null && object != null) {
						ContextItem newContextItem = new ContextItem();
						newContextItem.setObject(object);
						newContextItem.setPredicate(predicate);
						newContextItem.setSubject(subject);
						items.add(newContextItem);
					}
				}
			} else {
				message = responseElement.elementText("message");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isSuccess() {
		if(status == null || status.isEmpty() || !status.equals("200")) {
			return false;
		} else {
			return true;
		}
	}
}
