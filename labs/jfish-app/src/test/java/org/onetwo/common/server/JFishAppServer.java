package org.onetwo.common.server;

import org.onetwo.common.server.tomcat.TomcatServer;

public class JFishAppServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TomcatServer.create().start();
		/*ServerConfig config = null;
		try {
			config = TomcatConfig.getInstance().asServerConfig();
		} catch (Throwable e) {
			e.printStackTrace();
			config = new ServerConfig();
		}
		TomcatServer server = TomcatServer.create(config);
		server.start();*/
	}

}
