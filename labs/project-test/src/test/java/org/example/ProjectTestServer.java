package org.example;


import org.onetwo.common.web.server.tomcat.TomcatServer;

public class ProjectTestServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		TomcatServer.create().start();
	}

}
