package com.thesis.urgency.persistentStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.thesis.urgency.common.Case;
import com.thesis.urgency.common.ContextItem;
import com.thesis.urgency.common.MyCBRPatientCase;
import com.thesis.urgency.mycbr.CBREngine;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.BooleanDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.DescriptionEnum;
import de.dfki.mycbr.core.model.FloatDesc;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.io.XMLExporter;
import de.dfki.mycbr.util.Pair;

public class MyCBRPatientCasesPersistentStore implements PersistentStore {
	
	private CBREngine engine;
	private Project project;
	private DefaultCaseBase casebase;
	private Concept concept;
	
	public MyCBRPatientCasesPersistentStore() {
		engine = new CBREngine();
		project = engine.createProjectFromPRJ();
		casebase = (DefaultCaseBase)project.getCaseBases().get(engine.getCaseBase());
		concept = project.getConceptByID(engine.getConceptName());
	}

	@Override
	public Case getMostSimilarCase(ArrayList<ContextItem> subjectContext) {
		// TODO Auto-generated method stub
		Retrieval retrieval = new Retrieval(concept, casebase);
		retrieval.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
		Instance query = retrieval.getQueryInstance();
		
		setAttributesToInstance(query, contextToHashMap(subjectContext));
		
		retrieval.start();
		List<Pair<Instance, Similarity>> result = retrieval.getResult();
		
		System.out.println("result length = " + result.size());
		Iterator<Pair<Instance, Similarity>> i = result.iterator();
		while(i.hasNext()) {
			Pair<Instance, Similarity> pair = i.next();
			System.out.println(pair.getFirst().getValueAsString());
			System.out.println(pair.getSecond().getValue());
		}
		
		MyCBRPatientCase returnCase = new MyCBRPatientCase();
		Instance chosenCase = result.get(0).getFirst();
		
		IntegerDesc urgencyDesc = (IntegerDesc)concept.getAllAttributeDescs().get("urgency");
		Attribute urgencyAttribute = chosenCase.getAttForDesc(urgencyDesc);
		returnCase.setUrgency(urgencyAttribute.getValueAsString());

		return returnCase;
	}

	private void setAttributesToInstance(Instance instance, HashMap<String, String> context) {
		// TODO Auto-generated method stub
		Set attributeList = concept.getAllAttributeDescs().keySet();
		Iterator i = attributeList.iterator();
		while(i.hasNext()) {
			String attributeName = (String)i.next();
			try {
//				if(attributeName.equals("urgency")) {
//					continue;
//				}
				if(context.get(attributeName) == null) {
					continue;
				}
				AttributeDesc desc = concept.getAttributeDesc(attributeName);
				instance.addAttribute(desc, context.get(attributeName));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private HashMap<String, String> contextToHashMap(ArrayList<ContextItem> context) {
		if(context == null) {
			return null;
		}
		HashMap<String, String> result = new HashMap<String, String>();
		Iterator<ContextItem> i = context.iterator();
		while(i.hasNext()) {
			ContextItem item = i.next();
			if(item.getPredicate() != null && item.getPredicate() != null) {
				result.put(item.getPredicate(), item.getObject());
			}
		}
		return result;
	}

	@Override
	public void addCase(Case newCase) {
		// TODO Auto-generated method stub
		if(!(newCase instanceof MyCBRPatientCase)) {
			return;
		}
		Instance instance = new Instance(concept, concept.getName() + " #" + concept.getAllInstances().size());
		setAttributesToInstance(instance, contextToHashMap(((MyCBRPatientCase)newCase).getContext()));
		
		try {
			AttributeDesc desc = concept.getAttributeDesc("urgency");
			instance.addAttribute(desc, newCase.getUrgency());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//concept.addInstance(instance);
		casebase.addCase(instance);
		project.save();
	}
}
