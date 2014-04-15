package com.thesis.urgency.persistentStore;

import java.util.ArrayList;
import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;

public interface PersistentStore {
	
	public Case getMostSimilarCase(ArrayList<ContextItem> subjectContext);
	public void addCase(Case newCase);
}
