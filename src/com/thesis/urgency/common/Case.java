package com.thesis.urgency.common;

import java.util.ArrayList;

public interface Case {
	
	public String getUrgency();
	public ArrayList getContext();
	public void setUrgency(String urgency);
	public void setContext(ArrayList context);
}
