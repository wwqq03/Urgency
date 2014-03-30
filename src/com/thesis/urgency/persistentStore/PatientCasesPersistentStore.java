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
import com.thesis.urgency.common.PatientCase;

public class PatientCasesPersistentStore implements PersistentStore {
	
	private static final String PATIENTCASES = "patient cases.xml";
	private static ArrayList<Case> patientCases;
	
	@Override
	public ArrayList<Case> getCases() {
		if(patientCases == null) {
			patientCases = new ArrayList<Case>();
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
						PatientCase newPatientCase = new PatientCase();
						newPatientCase.setContext(contextItems);
						newPatientCase.setUrgency(urgency);
						patientCases.add(newPatientCase);
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<Case> result = new ArrayList<Case>(patientCases);
		return result;
	}

	@Override
	public void addCase(Case newCase) {
		// TODO Auto-generated method stub
		PatientCase newPatientCase;
		if(patientCases == null) {
			return;
		} else if(newCase == null) {
			return;
		} else if(!(newCase instanceof PatientCase)) {
			return;
		} else {
			newPatientCase = (PatientCase) newCase;
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
			
			patientCases.add(newCase);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
