package com.thesis.urgency;

import java.util.ArrayList;
import java.util.Iterator;

import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;
import com.thesis.urgency.common.PatientCase;
import com.thesis.urgency.contextGathering.ContextCollector;
import com.thesis.urgency.persistentStore.PatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.PersistentStore;

public class Analyzer {
	
	private PersistentStore patientCasesPersistentStore;
	private ContextCollector collector;
	
	public Analyzer() {
		patientCasesPersistentStore = new PatientCasesPersistentStore();
		collector = new ContextCollector();
	}
	
	public String getUrgency(String subject) {
		String urgency = null;
		
		ArrayList<Case> patientCases = patientCasesPersistentStore.getCases();
		
		ArrayList<ContextItem> subjectContext = collector.getContext(subject);
		PatientCase subjectPatientCase = new PatientCase();
		subjectPatientCase.setContext(subjectContext);
		
		Case chosenCase = null;
		int chosenUrgency = 0;
		Iterator<Case> i = patientCases.iterator();
		while(i.hasNext()) {
			Case patientCase = i.next();
			if(patientCase.isMatch(subjectContext)) {
				PatientCase currentPatientCase = (PatientCase)patientCase;
				if(Integer.parseInt(currentPatientCase.getUrgency()) > chosenUrgency) {
					chosenUrgency = Integer.parseInt(currentPatientCase.getUrgency());
					chosenCase = currentPatientCase;
				}
			}
		}
		
		storeChosenCase(chosenCase);
		
		urgency = String.valueOf(chosenUrgency);
		return urgency;
	}

	private void storeChosenCase(Case chosenCase) {
		// TODO Auto-generated method stub
		
	}
}
