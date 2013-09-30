package org.example;


import org.onetwo.common.server.tomcat.TomcatServer;

public class ProjectTestServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		TomcatServer.create().start();
	}

}
