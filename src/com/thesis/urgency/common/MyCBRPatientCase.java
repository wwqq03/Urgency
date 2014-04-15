package com.thesis.urgency.common;

import java.util.ArrayList;

import com.thesis.urgency.persistentStore.PersistentStore;

public class MyCBRPatientCase implements Case {
	
	private String urgency;
	private ArrayList<ContextItem> context = new ArrayList<ContextItem>();
	
	public String getUrgency() {
		return urgency;
	}
	
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
	public ArrayList<ContextItem> getContext() {
		return context;
	}
	
	public void setContext(ArrayList<ContextItem> context) {
		this.context = context;
	}
}
