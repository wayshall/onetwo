package org.onetwo.common.web.server.tomcat;

import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.server.ServerConfig;

public class TomcatConfig extends AppConfig {
	
	public static final String PORT = "port";
	public static final String NAME = "app.name";
	public static final String WEBAPP_DIR = "webapp.dir";
	public static final String CONTEXT_PATH = "context.path";
	public static final String SERVER_BASE_DIR = "server.base.dir";
	


	private static TomcatConfig instance = new TomcatConfig();
	
	public static TomcatConfig getInstance() {
		return instance;
	}

	private TomcatConfig(){
		super("tomcat.properties");
	}
	
	public Integer getPort(){
		return getInteger(PORT, 8080);
	}
	
	public String getName(){
		return getProperty(NAME, "");
	}
	
	public String getWebappDir(){
		return getProperty(WEBAPP_DIR);
	}
	
	public String getContextpath(){
		return getProperty(CONTEXT_PATH, "/"+getName());
	}
	public String getServerBaseDir(){
		return getProperty(SERVER_BASE_DIR);
	}

	public ServerConfig asServerConfig(){
		ServerConfig conf = new ServerConfig();
		conf.setPort(getPort());
		conf.setAppName(getName());
		conf.setContextPath(getContextpath());
		conf.setWebappDir(getWebappDir());
		conf.setServerBaseDir(getServerBaseDir());
		return conf;
	}
	
}
