package org.onetwo.common.web.tomcatmini;

import java.io.InputStream;
import java.util.Properties;

import org.apache.tomcat.util.http.fileupload.IOUtils;

public class TomcatConfig {
	
	public static final String PORT = "port";
	public static final String NAME = "app.name";
	public static final String WEBAPP_DIR = "webapp.dir";
	public static final String CONTEXT_PATH = "context.path";
	public static final String SERVER_BASE_DIR = "server.base.dir";
	

	private static class TomcatConfigHolder {
		final private static TomcatConfig instance = new TomcatConfig();
	}
	
	public static TomcatConfig getInstance() {
		return TomcatConfigHolder.instance;
	}
	
	private Properties config;

	private TomcatConfig(){
		config = load("tomcat.properties");
	}
	
	public Integer getPort(){
		return getInteger(PORT, 8080);
	}
	
	public String getName(){
		return config.getProperty(NAME, "");
	}
	
	public String getWebappDir(){
		return config.getProperty(WEBAPP_DIR);
	}
	
	public String getContextpath(){
		return config.getProperty(CONTEXT_PATH, "/"+getName());
	}
	public String getServerBaseDir(){
		return config.getProperty(SERVER_BASE_DIR);
	}

	public Integer getInteger(String key) {
		return getInteger(key, Integer.valueOf(0));
	}
	
	public Integer getInteger(String key, Integer def) {
		if (!config.containsKey(key)) {
			return def;
		}
		Integer integer = null;
		try {
			integer = new Integer(config.getProperty(key));
		} catch (Exception e) {
			integer = def;
		}
		return integer;
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
	
	public static Properties load(String srcpath){
		Properties config = new Properties();
		try {
			config = loadProperties(srcpath);
		} catch (Exception e) {
			InputStream in = null;
			try {
				in = TomcatConfig.class.getResourceAsStream(srcpath);
				if(in==null)
					throw new RuntimeException("can load resource as stream with : " +srcpath );
				config.load(in);
			} catch (Exception e1) {
				throw new RuntimeException("load config error: " + srcpath, e);
			} finally{
				IOUtils.closeQuietly(in);
			}
		}
		return config;
	}
	
	public static Properties loadProperties(String configName) {
		InputStream inStream = TomcatConfig.class.getClassLoader().getResourceAsStream(configName);
		if(inStream==null)
			throw new RuntimeException("can load as stream with : " +configName );
		try {
			Properties properties = new Properties();
			properties.load(inStream);
			return properties;
		} catch (Exception e) {
			throw new RuntimeException("load config error : " + configName, e);
		}
	}
}
