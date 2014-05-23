package org.onetwo.common.web.server.tomcat;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
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
			Connector connector = tomcat.getConnector();
			connector.setURIEncoding("UTF-8");
			connector.setRedirectPort(webConfig.getRedirectPort());
			
			ProtocolHandler protocol = connector.getProtocolHandler();
			if(protocol instanceof AbstractHttp11Protocol){
				/*****
				 * <Connector port="8080" protocol="HTTP/1.1" 
					   connectionTimeout="20000" 
   						redirectPort="8181" compression="500" 
  						compressableMimeType="text/html,text/xml,text/plain,application/octet-stream" />
				 */
				AbstractHttp11Protocol hp = (AbstractHttp11Protocol) protocol;
				hp.setCompression("on");
				hp.setCompressableMimeTypes("text/html,text/xml,text/plain,application/octet-stream");
			}
			
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
