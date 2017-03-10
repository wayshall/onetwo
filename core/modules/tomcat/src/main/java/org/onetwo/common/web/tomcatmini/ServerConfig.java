package org.onetwo.common.web.tomcatmini;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public class ServerConfig {
	public static final String SLASH = "/";
	public static final String EMPTY = "";
	
	private int port = 8080;
	private String appName;
//	private String webappDir;
//	private String contextPath;
	private String serverBaseDir;
	private int redirectPort = 8443;
	
	private WebappConfig defaultWebappConfig;
	private final Collection<WebappConfig> webapps = new HashSet<WebappConfig>();
	private String tomcatContextClassName;
	
	private int maxPostSize = -1;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public int getMaxPostSize() {
		return maxPostSize;
	}

	public void setMaxPostSize(int maxPostSize) {
		this.maxPostSize = maxPostSize;
	}

	public ServerConfig addWebapp(String webappDir, String contextPath){
		webapps.add(new WebappConfig(webappDir, contextPath));
		return this;
	}
	public ServerConfig addWebappByProjectDir(String projectDir, String contextPath){
		String realPath = getProjectMainDir(projectDir)+appendStartWith("webapp", "/");
		webapps.add(new WebappConfig(realPath, contextPath));
		return this;
	}
	
	public ServerConfig addProjectBaseWebapp(String webappDir){
		String realPath = getProjectMainDir()+appendStartWith(webappDir, "/");
		webapps.add(new WebappConfig(realPath, webappDir));
		return this;
	}
	
	public ServerConfig addProjectBaseWebapp(String webappDir, String contextPath){
		String realPath = getProjectMainDir()+appendStartWith(webappDir, "/");
		webapps.add(new WebappConfig(realPath, contextPath));
		return this;
	}
	
	public Collection<WebappConfig> getWebapps() {
		return webapps;
	}

	private File getProjectDir(){
		String baseDirPath = getDefaultClassLoader().getResource("").getPath();//FileUtils.getResourcePath("");
		File baseDir = new File(baseDirPath);
		if(baseDir.getName().contains("classes")){
			baseDir = baseDir.getParentFile().getParentFile();
		}else{
			baseDir = baseDir.getParentFile();
		}
		return baseDir;
	}
	
	public String getProjectMainDir(){
		String mainDir = getProjectMainDir(getProjectDir().getPath());
		return mainDir;
	}
	
	public String getProjectMainDir(String projectDir){
		String mainDir = projectDir + "/src/main";
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
		if(Utils.isBlank(appName)){
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
		return serverBaseDir;
		/*if(Utils.isBlank(serverBaseDir))
			return getProjectDir().getPath() + "/target";
		if(serverBaseDir.startsWith("/")){
			return serverBaseDir;
		}
		return getProjectDir().getPath()+"/"+serverBaseDir;*/
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
	
	public static String appendStartWithSlash(String path) {
		return appendStartWith(path, SLASH);
	}

	public static String appendStartWith(String path, String prefix) {
		if (path == null)
			path = EMPTY;
		if (!path.startsWith(prefix)) {
			path = prefix + path;
		}
		return path;
	}

	public static ClassLoader getDefaultClassLoader(){
		ClassLoader cld = null;
		try {
			cld = Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
			//ignore
		}
		if(cld==null){
			cld = ServerConfig.class.getClassLoader();
		}
		return cld;
	}

	public String getTomcatContextClassName() {
		return tomcatContextClassName;
	}

	public void setTomcatContextClassName(String tomcatContextClassName) {
		this.tomcatContextClassName = tomcatContextClassName;
	}
	
}
