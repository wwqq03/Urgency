package com.thesis.urgency.common;

import java.util.ArrayList;

public interface Case {
	
	//biggerCase is the case with context getting from the Context Gathering
	boolean isMatch(ArrayList<ContextItem> biggerCase);
}
