package com.thesis.urgency;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		server.go();
	}
	
	public void go() {
		Analyzer analyzer = new Analyzer();
		System.out.println(analyzer.getUrgency("Anna"));
	}

}
