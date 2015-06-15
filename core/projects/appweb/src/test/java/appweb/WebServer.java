package appweb;


import org.onetwo.common.web.server.ServerConfig;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class WebServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setServerBaseDir("bin");
		config.setContextPath("/appweb");
		TomcatServer.create(config).start();
	}

}
