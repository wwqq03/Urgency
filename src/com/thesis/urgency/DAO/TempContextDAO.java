package com.thesis.urgency.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thesis.urgency.Server;
import com.thesis.urgency.common.ContextItem;

public class TempContextDAO {
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = Server.DB_URL;
	
	public static final String USER = Server.DB_USER;
	public static final String PASSWORD = Server.DB_PASSWORD;
	
	private static final String table = Server.DB_TABLE_CONTEXT_TEMP;
	
	public void store(String callId, ArrayList<ContextItem> context) {
		if(callId == null || context == null) {
			return;
		}
		
		String contextString = contextToXML(context);
		
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + table + " where "
					+ "callId='" + callId + "'";
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				rs.updateString("context", contextString);
				rs.updateRow();
			} else {
				rs.moveToInsertRow();
				rs.updateString("callId", callId);
				rs.updateString("context", contextString);
				rs.insertRow();
			}
			System.out.println("Database updated");
			
			rs.close();
			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				try{
					conn.close();
			    } catch(SQLException se) {
			    	se.printStackTrace();
			    }
			}
		}
	}
	
	public ArrayList<ContextItem> read(String callId) {
		if(callId == null || callId.isEmpty()){
			return null;
		}
		
		Connection conn = null;
		ArrayList<ContextItem> result = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			System.out.println("Database connected");
			
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + table + " where "
					+ "callId='" + callId + "'";
			
			ResultSet rs = statement.executeQuery(sql);
			String contextXML = null;
			if(rs.next()) {
				contextXML = rs.getString("context");
			}
			
			if(contextXML != null && !contextXML.isEmpty()) {
				result =  XMLToContext(contextXML);
			}
			
			rs.close();
			statement.close();
			conn.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(conn != null){
				try{
					conn.close();
			    } catch(SQLException se) {
			    	se.printStackTrace();
			    }
			}
		}
	}
	
	private ArrayList<ContextItem> XMLToContext(String xml) {
		ArrayList<ContextItem> result = new ArrayList<ContextItem>();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element caseElement = document.getRootElement();

			ArrayList<Element> contextElements = new ArrayList<Element>(caseElement.elements("context"));
			Iterator<Element> i = contextElements.iterator();
			while(i.hasNext()) {
				Element contextElement = i.next();
				String predicate = contextElement.elementText("predicate");
				String object = contextElement.elementText("object");
					
				if(predicate != null && object != null) {
					ContextItem newContextItem = new ContextItem();
					newContextItem.setObject(object);
					newContextItem.setPredicate(predicate);
					result.add(newContextItem);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private String contextToXML(ArrayList<ContextItem> context) {
		if(context == null) {
			return null;
		}
		String xml = null;
		try {
			Document document = DocumentHelper.createDocument();
	        Element caseElement = document.addElement("case");
	        
	        Iterator<ContextItem> iContextItem = context.iterator();
	        while(iContextItem.hasNext()) {
	        	ContextItem item = iContextItem.next();
	        	String predicate = item.getPredicate();
	        	String object = item.getObject();
	        	if(predicate != null && object != null) {
	        		Element contextElement = caseElement.addElement("context");
	        		Element predicateElement = contextElement.addElement("predicate");
	        		predicateElement.setText(predicate);
	        		Element objectElement = contextElement.addElement("object");
	        		objectElement.setText(object);
	        	}
	        }
	        xml = caseElement.asXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
}
