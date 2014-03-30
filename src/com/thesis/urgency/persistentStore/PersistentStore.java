package com.thesis.urgency.persistentStore;

import java.util.ArrayList;
import com.thesis.urgency.common.Case;

public interface PersistentStore {
	
	public ArrayList<Case> getCases();
	public void addCase(Case newCase);
}
