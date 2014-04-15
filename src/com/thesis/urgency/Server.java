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
		Analyzer analyzer = new Analyzer(Analyzer.MYCBR);
		System.out.println("The urgency is: " + analyzer.getUrgency("Mike"));
	}

}
