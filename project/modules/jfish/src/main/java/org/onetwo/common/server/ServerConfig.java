package org.onetwo.common.server;

import java.io.File;

import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;

public class ServerConfig {
	private int port = 8080;
	private String appName;
	private String webappDir;
	private String contextPath;
	private String serverBaseDir;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	private File getProjectDir(){
		String baseDirPath = FileUtils.getResourcePath("");
		File baseDir = new File(baseDirPath);
		baseDir = baseDir.getParentFile().getParentFile();
		return baseDir;
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
		if(StringUtils.isBlank(webappDir)){
			webappDir = getProjectDir().getPath() + "/src/main/webapp";
		}
		return webappDir;
	}

	public void setWebappDir(String webappDir) {
		this.webappDir = webappDir;
	}

	public String getContextPath() {
		if(StringUtils.isBlank(contextPath)){
			return "/"+getAppName();
		}
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getServerBaseDir() {
		if(StringUtils.isBlank(serverBaseDir))
			return getWebappDir();
		return serverBaseDir;
	}

	public void setServerBaseDir(String serverBaseDir) {
		this.serverBaseDir = serverBaseDir;
	}
}
