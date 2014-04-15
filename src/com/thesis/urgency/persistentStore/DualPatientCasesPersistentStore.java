package com.thesis.urgency.persistentStore;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;
import com.thesis.urgency.common.DualPatientCase;

public class DualPatientCasesPersistentStore implements PersistentStore {
	
	private static final String PATIENTCASES = "patient cases.xml";
	private static ArrayList<DualPatientCase> patientCases;
	
	@Override
	public Case getMostSimilarCase(ArrayList<ContextItem> subjectContext) {
		if(patientCases == null) {
			patientCases = new ArrayList<DualPatientCase>();
			try {
				
				SAXReader reader = new SAXReader();
				Document  document = reader.read(new File(PATIENTCASES));
				
				Element cases = document.getRootElement();
				List<Element> caseList = cases.elements("case");
				
				Iterator<Element> itCase = caseList.iterator();
				while(itCase.hasNext()) {
					Element caseElement = itCase.next();
					Attribute urgencyAttribute = caseElement.attribute("urgency");
					if(urgencyAttribute == null) {
						continue;
					}
					String urgency = urgencyAttribute.getText();
					ArrayList<ContextItem> contextItems = new ArrayList<ContextItem>();
					
					List<Element> context = caseElement.elements("context");
					Iterator<Element> itContext = context.iterator();
					while(itContext.hasNext()) {
						Element contextElement = itContext.next();
						String predicate = contextElement.elementText("predicate");
						String object = contextElement.elementText("object");
						if(predicate != null
								&& !predicate.isEmpty()
								&& object != null
								&& !object.isEmpty()) {
							ContextItem newItem = new ContextItem();
							newItem.setObject(object);
							newItem.setPredicate(predicate);
							contextItems.add(newItem);
						}
					}
					
					if(urgency != null
							&& !urgency.isEmpty()
							&& !contextItems.isEmpty()) {
						DualPatientCase newPatientCase = new DualPatientCase();
						newPatientCase.setContext(contextItems);
						newPatientCase.setUrgency(urgency);
						patientCases.add(newPatientCase);
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		DualPatientCase chosenCase = null;
		int chosenUrgency = 0;
		Iterator<DualPatientCase> i = patientCases.iterator();
		while(i.hasNext()) {
			DualPatientCase patientCase = i.next();
			if(patientCase.isMatch(subjectContext)) {
				DualPatientCase currentPatientCase = (DualPatientCase)patientCase;
				if(Integer.parseInt(currentPatientCase.getUrgency()) > chosenUrgency) {
					chosenUrgency = Integer.parseInt(currentPatientCase.getUrgency());
					chosenCase = currentPatientCase;
				}
			}
		}
		DualPatientCase result = new DualPatientCase();
		result.setContext(new ArrayList<ContextItem>(chosenCase.getContext()));
		result.setUrgency(chosenCase.getUrgency());
		return result;
	}

	@Override
	public void addCase(Case newCase) {
		// TODO Auto-generated method stub
		DualPatientCase newPatientCase;
		if(patientCases == null) {
			return;
		} else if(newCase == null) {
			return;
		} else if(!(newCase instanceof DualPatientCase)) {
			return;
		} else {
			newPatientCase = (DualPatientCase) newCase;
		}
		
		if(newPatientCase.getUrgency() == null || newPatientCase.getContext() == null || newPatientCase.getContext().isEmpty()) {
			return;
		}
		
		try {
			SAXReader reader = new SAXReader();
			Document  document = reader.read(new File(PATIENTCASES));
			Element casesElement = document.getRootElement();
			Element newCaseElement = casesElement.addElement("case");
			
			newCaseElement.addAttribute("urgency", newPatientCase.getUrgency());
			Iterator<ContextItem> i = newPatientCase.getContext().iterator();
			while(i.hasNext()) {
				ContextItem item = i.next();
				if(item.getSubject() == null || item.getObject() == null) {
					continue;
				}
				Element contextElement = newCaseElement.addElement("context");
				Element predicateElement = contextElement.addElement("predicate");
				predicateElement.setText(item.getPredicate());
				Element objectElement = contextElement.addElement("object");
				objectElement.setText(item.getObject());
			}
			
			patientCases.add((DualPatientCase)newCase);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
