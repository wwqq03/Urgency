package com.thesis.urgency.common;

import java.util.ArrayList;
import java.util.Iterator;

public class DualPatientCase implements Case {
	
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

	//contextToCompare is the case with context getting from the Context Gathering
	public boolean isMatch(ArrayList<ContextItem> contextToCompare) {
		if(contextToCompare == null || contextToCompare.isEmpty()) {
			return false;
		}
		
		Iterator<ContextItem> i = context.iterator();
		while(i.hasNext()) {
			ContextItem item = i.next();
			if(!contextToCompare.contains(item)) {
				return false;
			}
		}
		return true;
	}
}
