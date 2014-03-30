package com.thesis.urgency.common;

public class ContextItem {
	
	private String subject;
	private String predicate;
	private String object;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	@Override
	public boolean equals(Object c) {
		if(! (c instanceof ContextItem)) {
			return false;
		}
		ContextItem item = (ContextItem)c;
		if(item.predicate.equals(this.predicate) && item.object.equals(this.object)) {
			return true;
		}
		return false;
	}
}
