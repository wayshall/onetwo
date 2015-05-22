package org.onetwo.common.web.server;

import java.io.File;
import java.util.Collection;

import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class ServerConfig {
	private int port = 8080;
	private String appName;
//	private String webappDir;
//	private String contextPath;
	private String serverBaseDir;
	private int redirectPort = 8443;
	
	private WebappConfig defaultWebappConfig;
	private final Collection<WebappConfig> webapps = LangUtils.newHashSet();

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public ServerConfig addWebapp(String webappDir, String contextPath){
		webapps.add(new WebappConfig(webappDir, contextPath));
		return this;
	}
	
	public ServerConfig addProjectBaseWebapp(String webappDir){
		String realPath = getProjectMainDir()+StringUtils.appendStartWith(webappDir, "/");
		webapps.add(new WebappConfig(realPath, webappDir));
		return this;
	}
	
	public ServerConfig addProjectBaseWebapp(String webappDir, String contextPath){
		String realPath = getProjectMainDir()+StringUtils.appendStartWith(webappDir, "/");
		webapps.add(new WebappConfig(realPath, contextPath));
		return this;
	}
	
	public Collection<WebappConfig> getWebapps() {
		return webapps;
	}

	private File getProjectDir(){
		String baseDirPath = FileUtils.getResourcePath("");
		File baseDir = new File(baseDirPath);
		if(baseDir.getName().contains("classes")){
			baseDir = baseDir.getParentFile().getParentFile();
		}else{
			baseDir = baseDir.getParentFile();
		}
		return baseDir;
	}
	
	public String getProjectMainDir(){
		String mainDir = getProjectDir().getPath() + "/src/main";
		return mainDir;
	}

	public WebappConfig getDefaultWebappConfig() {
		if(defaultWebappConfig==null){
			defaultWebappConfig  = new WebappConfig();
			defaultWebappConfig.setContextPath("/"+getAppName());
			defaultWebappConfig.setWebappDir(getProjectMainDir() + "/webapp");
		}
		return defaultWebappConfig;
	}

	public String getAppName() {
		if(StringUtils.isBlank(appName)){
			appName = getProjectDir().getName();
		}
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWebappDir() {
		/*if(StringUtils.isBlank(webappDir)){
			webappDir = getProjectDir().getPath() + "/src/main/webapp";
		}
		return webappDir;*/
		return getDefaultWebappConfig().getWebappDir();
	}

	public void setWebappDir(String webappDir) {
		this.getDefaultWebappConfig().setWebappDir(webappDir);
	}

	public String getContextPath() {
		/*if(StringUtils.isBlank(contextPath)){
			return "/"+getAppName();
		}
		return contextPath;*/
		return getDefaultWebappConfig().getContextPath();
	}

	public void setContextPath(String contextPath) {
//		this.contextPath = contextPath;
		getDefaultWebappConfig().setContextPath(contextPath);
	}

	public String getServerBaseDir() {
		if(StringUtils.isBlank(serverBaseDir))
			return getProjectDir().getPath() + "/target";
		return serverBaseDir;
	}

	public void setServerBaseDir(String serverBaseDir) {
		this.serverBaseDir = serverBaseDir;
	}

	public int getRedirectPort() {
		return redirectPort;
	}

	public void setRedirectPort(int redirectPort) {
		this.redirectPort = redirectPort;
	}
	
}
