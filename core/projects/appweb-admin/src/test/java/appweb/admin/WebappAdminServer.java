package appweb.admin;


import org.onetwo.common.web.server.ServerConfig;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class WebappAdminServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setServerBaseDir("bin");
		config.setPort(9080);
		TomcatServer.create(config).start();
	}

}
