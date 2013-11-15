package org.onetwo.common.web.server.tomcat;

import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.web.server.ServerConfig;

public class TomcatServer {

	public static TomcatServer create(){
		ServerConfig config = null;
		try {
			config = TomcatConfig.getInstance().asServerConfig();
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			config = new ServerConfig();
		}
		return create(config);
	}

	public static TomcatServer create(ServerConfig webConfig){
		return new TomcatServer(webConfig);
	}
	
	private ServerConfig webConfig;
	private Tomcat tomcat;

	private TomcatServer(ServerConfig webConfig) {
		this.webConfig = webConfig;
	}

	public void start() {
		try {
			this.tomcat = new Tomcat();
			int port = webConfig.getPort();
			tomcat.setPort(port);
			tomcat.setBaseDir(webConfig.getServerBaseDir());
			tomcat.getHost().setAppBase(webConfig.getServerBaseDir());
			tomcat.getConnector().setURIEncoding("UTF-8");

			StandardServer server = (StandardServer) tomcat.getServer();
			AprLifecycleListener listener = new AprLifecycleListener();
			server.addLifecycleListener(listener);

			tomcat.addWebapp(webConfig.getContextPath(), webConfig.getWebappDir());
			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception e) {
			throw new BaseException("web server start error , check it. " + e.getMessage(), e);
		}
	}
}
