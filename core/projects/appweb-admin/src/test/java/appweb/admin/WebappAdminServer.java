package appweb.admin;


import org.onetwo.common.web.server.tomcat.TomcatServer;

public class WebappAdminServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		TomcatServer.create().start();
	}

}
