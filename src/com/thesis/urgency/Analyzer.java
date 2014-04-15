package com.thesis.urgency;

import java.util.ArrayList;
import java.util.Iterator;

import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;
import com.thesis.urgency.common.DualPatientCase;
import com.thesis.urgency.contextGathering.ContextCollector;
import com.thesis.urgency.persistentStore.DualPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.MyCBRPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.PersistentStore;

public class Analyzer {
	
	public static final int MYCBR = 1;
	public static final int DUAL = 2;
	
	private PersistentStore patientCasesPersistentStore;
	private ContextCollector collector;
	private int mode;
	
	public Analyzer(int mode) {
		this.mode = mode;
		if(mode == Analyzer.MYCBR) {
			patientCasesPersistentStore = new MyCBRPatientCasesPersistentStore();
		} else if(mode == DUAL) {
			patientCasesPersistentStore = new DualPatientCasesPersistentStore();
		}
		collector = new ContextCollector();
	}
	
	public String getUrgency(String subject) {
		if(patientCasesPersistentStore == null) {
			return "0";
		}
		
		ArrayList<ContextItem> subjectContext = collector.getContext(subject);
		Case chosenCase = patientCasesPersistentStore.getMostSimilarCase(subjectContext);
		if(chosenCase == null) {
			return "0";
		}
		
		storeChosenCase(chosenCase);
		
		return chosenCase.getUrgency();
	}

	private void storeChosenCase(Case chosenCase) {
		// TODO Auto-generated method stub
		
	}
}
