package qingxinkd;


import org.onetwo.common.web.server.ServerConfig;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class QingxinkdWebServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setPort(9080);
		TomcatServer.create(config).start();
	}

}
