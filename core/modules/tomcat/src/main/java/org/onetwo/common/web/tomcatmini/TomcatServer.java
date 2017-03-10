package org.onetwo.common.web.tomcatmini;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatServer {
	
	public static class TomcatServerBuilder {
		
		private ServerConfig serverConfig = new ServerConfig();
		static private Map<String, String> initParametersMapper = new HashMap<String, String>();

		public TomcatServerBuilder mapInitParameter(String name, String value){
			initParametersMapper.put(name, value);
			return this;
		}
		
		public static TomcatServerBuilder create(int port){
			TomcatServerBuilder builder = new TomcatServerBuilder();
			builder.serverConfig.setPort(port);	
			return builder;
		}
		
		public TomcatServerBuilder tomcatContextClassName(String tomcatContextClassName){
			this.serverConfig.setTomcatContextClassName(tomcatContextClassName);
			return this;
		}

		public TomcatServerBuilder addWebapp(String webappDir, String contextPath){
			this.serverConfig.addWebapp(webappDir, contextPath);
			return this;
		}
		public TomcatServerBuilder addWebappByProjectDir(String webappDir, String contextPath){
			this.serverConfig.addWebappByProjectDir(webappDir, contextPath);
			return this;
		}
		public TomcatServerBuilder addProjectBaseWebapp(String webappDir, String contextPath){
			this.serverConfig.addProjectBaseWebapp(webappDir, contextPath);
			return this;
		}

		public TomcatServerBuilder config(Consumer<ServerConfig> configer){
			configer.accept(this.serverConfig);
			return this;
		}
		
		public TomcatServer build(){
			if(!initParametersMapper.isEmpty()){
				serverConfig.setTomcatContextClassName(HardCodeSpringProfileContext.class.getName());
			}
			TomcatServer server = TomcatServer.create(serverConfig);
			return server;
		}
		
		public static Map<String, String> getInitParametersMapper() {
			return initParametersMapper;
		}



		/*
		 * tomcat通过getConstructor()实例化，无法使用innerclass，使用静态参数代替。。。
		 * public class HardCodeSpringProfileContext extends HackServletContextStandardContext {
			public HardCodeSpringProfileContext() {
//				this.mapInitParameter("spring.profiles.active", "dev");
				initParametersMapper.entrySet().forEach(e->this.mapInitParameter(e.getKey(), e.getValue()));
			}
		}*/
	}
	

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

	public static TomcatServer create(int prot){
		ServerConfig config = new ServerConfig();
		config.setPort(prot);
		TomcatServer tomcat = new TomcatServer(config);
		tomcat.initialize();
		return tomcat;
	}

	public static TomcatServer create(ServerConfig webConfig){
		TomcatServer tomcat = new TomcatServer(webConfig);
		tomcat.initialize();
		return tomcat;
	}

//	public static final String PLUGIN_WEBAPP_BASE_PATH = "classpath:META-INF";
	
	private ServerConfig serverConfig;
	private Tomcat tomcat;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<TomcatServerListener> listeners = new ArrayList<>();
	private File appBaseFile;

	private TomcatServer(ServerConfig webConfig) {
		this.serverConfig = webConfig;
	}
	
	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public void addListeners(TomcatServerListener...listeners){
		this.listeners.addAll(Arrays.asList(listeners));
	}
	public void initialize() {
		try {
			JFishTomcat tomcat = new JFishTomcat();
			if(serverConfig.getTomcatContextClassName()!=null){
				tomcat.setContextClass((Class<StandardContext>)ReflectUtils.loadClass(serverConfig.getTomcatContextClassName(), true));
			}
			int port = serverConfig.getPort();
			tomcat.setPort(port);
//			tomcat.setBaseDir(webConfig.getServerBaseDir());
			String baseDir = null;
			if(!Utils.isBlank(serverConfig.getServerBaseDir())){
				baseDir = serverConfig.getServerBaseDir();
			}else{
				baseDir = System.getProperty("java.io.tmpdir");
				logger.info("set serverBaseDir as java.io.tmpdir : {} ", baseDir);
			}
			tomcat.setBaseDir(baseDir);
			
			// This magic line makes Tomcat look for WAR files in catalinaHome/webapps
			// and automatically deploy them
//			tomcat.getHost().addLifecycleListener(new HostConfig());
			appBaseFile = new File(baseDir+"/tomcat.webapps."+this.serverConfig.getPort());
			if(!appBaseFile.exists()){
				appBaseFile.mkdirs();
			}
			appBaseFile.deleteOnExit();
			tomcat.getHost().setAppBase(appBaseFile.getAbsolutePath());
			Connector connector = tomcat.getConnector();
			connector.setURIEncoding("UTF-8");
			connector.setRedirectPort(serverConfig.getRedirectPort());
			connector.setMaxPostSize(serverConfig.getMaxPostSize());
			
			ProtocolHandler protocol = connector.getProtocolHandler();
			if(protocol instanceof AbstractHttp11Protocol){
				/*****
				 * <Connector port="8080" protocol="HTTP/1.1" 
					   connectionTimeout="20000" 
   						redirectPort="8181" compression="500" 
  						compressableMimeType="text/html,text/xml,text/plain,application/octet-stream" />
				 */
				AbstractHttp11Protocol<?> hp = (AbstractHttp11Protocol<?>) protocol;
				hp.setCompression("on");
				hp.setCompressableMimeTypes("text/html,text/xml,text/plain,application/octet-stream");
			}
			
			
			StandardServer server = (StandardServer) tomcat.getServer();
			AprLifecycleListener listener = new AprLifecycleListener();
			server.addLifecycleListener(listener);

			/*tomcat.addUser("adminuser", "adminuser");
			tomcat.addRole("adminuser", "admin");
			tomcat.addRole("adminuser", "admin");*/
			
			this.tomcat = tomcat;
		} catch (Exception e) {
			throw new RuntimeException("web server initialize error , check it. " + e.getMessage(), e);
		}
		
		/*Runtime.getRuntime().addShutdownHook(new Thread(){

			@Override
			public void run() {
				appBaseFile.delete();
			}
			
		});*/
	}

	public void start() {
		try {
			List<Context> contexts = addWebapps();
			this.onContextCreated(contexts);
			tomcat.start();
			printConnectors();
			tomcat.getServer().await();
		} catch (Exception e) {
//			logger.error("web server start error , check it. ", e);
			throw new RuntimeException("web server start error , check it. " + e.getMessage(), e);
		}
	}
	
	protected List<Context> addWebapps() throws ServletException{
		List<Context> contexts = new ArrayList<Context>();
		//host.addChild(ctx);
		File webAppDir = new File(serverConfig.getWebappDir());
		if(webAppDir.exists()){
			logger.info("add defulat webapp {}, path {} ", serverConfig.getContextPath(), serverConfig.getWebappDir());
			Context ctx = tomcat.addWebapp(serverConfig.getContextPath(), serverConfig.getWebappDir());
			/*if(ctx instanceof StandardContext){
				StandardContext stdCtx = (StandardContext)ctx;
				Wrapper defaultContainer = (Wrapper)stdCtx.findChild("default");
				defaultContainer.getMultipartConfigElement().
			}*/
			contexts.add(ctx);
		}
		for(WebappConfig webapp : serverConfig.getWebapps()){
			logger.info("add webapp : {} ", webapp);
			Context ctx = tomcat.addWebapp(webapp.getContextPath(), webapp.getWebappDir());
			contexts.add(ctx);
		}
		return contexts;
	}
	
	protected void onContextCreated(List<Context> contexts){
		this.listeners.forEach(l->l.onContextCreated(new ContextCreatedEvent(tomcat, contexts)));
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
