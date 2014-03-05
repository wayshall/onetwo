package com.qyscard.o2o;


import org.onetwo.common.web.server.ServerConfig;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class O2OWebServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setContextPath("/o2o");
		TomcatServer.create(config).start();
	}

}
