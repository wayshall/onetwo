package stemplate;


import org.onetwo.common.server.tomcat.TomcatServer;

public class WebServer {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		TomcatServer.create().start();
	}

}
