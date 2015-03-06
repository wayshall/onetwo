package org.onetwo.common.web.server;

public class WebappConfig {
	
	private String webappDir;
	private String contextPath;
	
	
	public WebappConfig() {
		super();
	}
	public WebappConfig(String webppDir, String contextPath) {
		super();
		this.webappDir = webppDir;
		this.contextPath = contextPath;
	}
	
	public String getWebappDir() {
		return webappDir;
	}
	public void setWebappDir(String webappDir) {
		this.webappDir = webappDir;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	@Override
	public String toString() {
		return "WebappConfig [webappDir=" + webappDir + ", contextPath="
				+ contextPath + "]";
	}
	
	
}
