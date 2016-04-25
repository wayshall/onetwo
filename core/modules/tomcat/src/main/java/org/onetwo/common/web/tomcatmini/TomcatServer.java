package org.onetwo.common.web.tomcatmini;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatServer {

	public static TomcatServer create(){
		ServerConfig config = null;
		try {
			config = TomcatConfig.getInstance().asServerConfig();
		} catch (Throwable e) {
			System.out.println("load config error: "+e.getMessage());
			System.out.println("use default config...");
			config = new ServerConfig();
		}
		return create(config);
	}

	public static TomcatServer create(ServerConfig webConfig){
		TomcatServer tomcat = new TomcatServer(webConfig);
		tomcat.initialize();
		return tomcat;
	}

//	public static final String PLUGIN_WEBAPP_BASE_PATH = "classpath:META-INF";
	
	private ServerConfig webConfig;
	private Tomcat tomcat;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private TomcatServer(ServerConfig webConfig) {
		this.webConfig = webConfig;
	}

	public void initialize() {
		try {
			
			this.tomcat = new JFishTomcat();
			int port = webConfig.getPort();
			tomcat.setPort(port);
//			tomcat.setBaseDir(webConfig.getServerBaseDir());
			tomcat.setBaseDir(webConfig.getServerBaseDir());
			tomcat.getHost().setAppBase(webConfig.getWebappDir());
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

			/*tomcat.addUser("adminuser", "adminuser");
			tomcat.addRole("adminuser", "admin");
			tomcat.addRole("adminuser", "admin");*/
		} catch (Exception e) {
			throw new RuntimeException("web server initialize error , check it. " + e.getMessage(), e);
		}
	}

	public void start() {
		try {

			addWebapps();
			tomcat.start();
			printConnectors();
			tomcat.getServer().await();
		} catch (Exception e) {
//			logger.error("web server start error , check it. ", e);
			throw new RuntimeException("web server start error , check it. " + e.getMessage(), e);
		}
	}
	
	protected void addWebapps() throws ServletException{
		logger.info("add defulat webapp {}, path {} ", webConfig.getContextPath(), webConfig.getWebappDir());
		tomcat.addWebapp(webConfig.getContextPath(), webConfig.getWebappDir());
		for(WebappConfig webapp : webConfig.getWebapps()){
			logger.info("add webapp : {} ", webapp);
			tomcat.addWebapp(webapp.getContextPath(), webapp.getWebappDir());
		}
	}
	
	public Tomcat getTomcat() {
		return tomcat;
	}

	protected void printConnectors(){
		Connector[] cons = tomcat.getService().findConnectors();
		for(Connector con : cons){
			System.out.println("Connector: " + con);
		}
	}
	
	protected void addSSLConnector(){
		Connector connector = new Connector("HTTP/1.1");
        // connector = new Connector("org.apache.coyote.http11.Http11Protocol"); 
        connector.setPort(8443);
	}
}
