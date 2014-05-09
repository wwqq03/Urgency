package com.thesis.urgency;

import java.io.FileInputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.thesis.urgency.persistentStore.DualPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.MyCBRPatientCasesPersistentStore;
import com.thesis.urgency.persistentStore.PersistentStore;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		server.go();
	}
	
	public static String URGENCY_PORT;
	public static String FEEDBACK_PORT;
	
	public static String CONTEXT_GATHERING_IP;
	public static String CONTEXT_GATHERING_PORT;
	
	public static String DB_URL;
	public static String DB_USER;
	public static String DB_PASSWORD;
	public static String DB_TABLE_CONTEXT_TEMP;
	
	public static String MYCBR_PATH;
	public static String MYCBR_PROJECT;
	public static String MYCBR_CONCEPT;
	public static String MYCBR_CASEBASE;
	
	public void go() {
		
		Properties p = new Properties();
		try{
			FileInputStream inputFile = new FileInputStream("config.txt"); 
			p.load(inputFile);
			
			URGENCY_PORT = p.getProperty("urgency_port");
			FEEDBACK_PORT = p.getProperty("feedback_port");
			
			CONTEXT_GATHERING_IP = p.getProperty("context_gathering_ip");
			CONTEXT_GATHERING_PORT = p.getProperty("context_gathering_port");
			
			DB_URL = p.getProperty("db_url");
			DB_USER = p.getProperty("db_user");
			DB_PASSWORD = p.getProperty("db_password");
			DB_TABLE_CONTEXT_TEMP = p.getProperty("db_table_context_temp");
			
			MYCBR_PATH = p.getProperty("myCBR_path");
			MYCBR_PROJECT = p.getProperty("myCBR_project");
			MYCBR_CONCEPT = p.getProperty("myCBR_concept");
			MYCBR_CASEBASE = p.getProperty("myCBR_casebase");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		BlockingQueue<Socket> urgencyRequestQueue = new ArrayBlockingQueue<Socket>(300);
		
		Thread urgencyRequestReceiver = new Thread(new UrgencyRequestReceiver(urgencyRequestQueue));
		urgencyRequestReceiver.start();
		
		PersistentStore persistentStore = new MyCBRPatientCasesPersistentStore();
		//PersistentStore persistentStore = new DualPatientCasesPersistentStore();
		Thread analyzer = new Thread(new Analyzer(persistentStore, urgencyRequestQueue));
		analyzer.start();
		
		BlockingQueue<Socket> feedbackRequestQueue = new ArrayBlockingQueue<Socket>(300);
		
		Thread feedbackRequestReceiver = new Thread(new FeedbackRequestReceiver(feedbackRequestQueue));
		feedbackRequestReceiver.start();
		
		Thread adjustor = new Thread(new Adjustor(persistentStore, feedbackRequestQueue));
		adjustor.start();
	}

}
