package com.thesis.urgency.mycbr;

import de.dfki.mycbr.core.Project;

public class CBREngine {
	
	private static final String data_path = "/Users/which/Documents/workspace/Urgency/";
	private static final String projectName = "Urgency.prj";
	private static final String conceptName = "PatientCases";
	private static final String casebase = "casebase";
	
	// Getter for the Project meta data
	public static String getCaseBase() {
		return casebase;
	}

	public static String getProjectName() {
		return projectName;
	}

	public static String getConceptName() {
		return conceptName;
	}
	
	/**
	 * This methods loads a myCBR project from a .prj file and loads the cases in this project.
	 * The specification of the project's location and according file names has to be
	 * done at the beginning of this class.
	 * @return Project instance containing model, sims and cases (if available)
	 */
	public Project createProjectFromPRJ(){

		Project project = null;

		try {
			// load new project
			project = new Project(data_path+projectName);
			// create a concept and get the main concept of the project; 
			// the name has to be specified at the beginning of this class
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");
		}

		catch(Exception ex) {

			System.out.println("Error loading .prj file");

		}

		return project;
	}
}
