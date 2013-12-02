package com.qyscard.exchange;


import org.onetwo.common.web.server.ServerConfig;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class DataexchangeServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setContextPath("/dataexchange");
		TomcatServer.create(config).start();
	}

}
