package org.onetwo.common.web.config;

import java.net.URL;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.VariableSupporterProvider;
import org.onetwo.common.utils.propconf.VariableExpositor;
import org.onetwo.common.web.s2.ext.LocaleUtils;
import org.onetwo.common.web.utils.StrutsUtils;
import org.onetwo.common.web.utils.WebVariableSupporterProvider;


public class SiteConfig extends BaseSiteConfig {
	protected static final Logger logger = Logger.getLogger(SiteConfig.class);

	public static final String CONFIG_NAME = "siteConfig";
	public static final String STRUTS_AOTUOLANGUAGE = "struts.aotuolanguage";

	public static final String DG_LOADING_PAGEFOOT = "dg.loading.pagefoot";
	public static final String DG_LOADING_CENTER = "dg.loading.center";
	
	public static final String LANGUAGE_SUPPORT = "language.support";
	
	public static final String SITE_DEFAULT_IMAGE = "site.default.image";

	public static final String APPSERVER = "appserver";
	public static final String APPLICATION_NAME = "application.name";
	public static final String BASEURL = "baseURL";
	public static final String SOLR_HOME = "solr.solr.home";
	
	public static final String DOMAIN = "site.domain";
	public static final String DOMAIN_PORT = "site.domain.port";

	public static final String COOKIE_LANGUAGE_AGE = "cookie.language.age";

	public static final String USER_DETAIL_KEY = "user.detail.key";
	public static final String TOKEN_NAME = "token.name";
	
	public static final String TOKEN_TIMEOUT = "token.timeout";
	public static final String TOKEN_TIMEOUT_CHECKTIME = "token.timeout.checktime";
	public static final Integer DEFAULT_TOKEN_TIMEOUT = 60*4;
	
	
	private ServletContext servletContext;
	private String contextPath;
	private String domain;
	private String domainContext;
	
	private static SiteConfig siteConfig = new SiteConfig();
//	private static final String CONFIG_FILE = "siteConfig-base.properties";
 
	protected SiteConfig() {
		super(CONFIG_FILE);
	}

	protected void initAppConfig(){
		super.initAppConfig();
		VariableSupporterProvider vp = new WebVariableSupporterProvider(this);
		expositor = new VariableExpositor(vp, true);
//		expositor = new VariableExpositor(this, new WebVariableSupporterProvider(), true);
	}

	public static SiteConfig getInstance() {
		return siteConfig;
	}

	public static SiteConfig inst() {
		return getInstance();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public String findClasspathResource(String path){
		return findResource("/WEB-INF/classes"+path);
	}
	
	public String findResource(String dir, String name){
		if(!dir.endsWith("/"))
			dir = dir + "/";
		return findResource(dir + name);
	}
	
	public String findResource(String path){
		if(StringUtils.isBlank(path))
			return null;
		if(!path.startsWith("/"))
			path = "/" + path;
		URL url = null;
		try {
			String realpath = getServletContext().getRealPath(path);
//			System.out.println("resource path: " + path);
			if(StringUtils.isNotBlank(realpath) && FileUtils.exists(realpath))
				return realpath;
			url = getServletContext().getResource(path);
		} catch (Exception e) {
			logger.error("get resource form classpath error!", e);
		}
		if(url==null)
			return null;
		return url.getPath();
	}

	public SiteConfig initWeb(FilterConfig fconfig) {
		servletContext = fconfig.getServletContext();
		String cp = servletContext.getContextPath();
		this.setContextPath(cp);
		String realPath = fconfig.getServletContext().getRealPath("");
		this.setProperty("contextRealPath", realPath);
		if(logger.isInfoEnabled()){
			logger.info("set contextPath: " + cp );
			logger.info("set ContextRealPath: " + realPath);
		}
		return this;
	}
	
	public static String getConfig(String key) {
		return siteConfig.getProperty(key, "");
	}

	public static int getConfigForInt(String key) {
		return siteConfig.getInt(key, 0);
	}

	public static long getConfigForLong(String key) {
		return siteConfig.getLong(key, 0l);
	}
	
	/***************************************************************************
	 * base
	 */
	
	public String getErrorPage(){
		return this.getProperty(ERROR_PAGE);
	}

	public int getErrorPageCode(){
		String errorPage = getErrorPage();
		int statusCode = -1;
		if(errorPage!=null){
			try {
				statusCode = Integer.parseInt(errorPage.trim());
			} catch (Exception e) {
			}
		}
		return statusCode;
	}
	
	public String getSolrHome(){
		return this.getProperty(SOLR_HOME);
	}

	public int getCookieLanguageAge() {
		return siteConfig.getInt(COOKIE_LANGUAGE_AGE, 60 * 60 * 24 * 7);
	}

	/**
	 * 包含语言版本
	 * 
	 * @return
	 */
	public String getBaseURL() {
		String baseURL = getVariable(BASEURL);
		if (StringUtils.isBlank(baseURL)){
			if(!containsKey(BASEURL))//如果没有配置，才赋值默认的上下文路径
				baseURL = contextPath;
		}
		if(LocaleUtils.getDefault().equals(StrutsUtils.getCurrentSessionLocale()))
			return baseURL;
		else
			return MyUtils.append(baseURL, "/", StrutsUtils.getSessionLanguage());
	}
	
	public String getBaseLangURL() {
		String baseURL = getVariable(BASEURL);
		if (StringUtils.isBlank(baseURL))
			baseURL = contextPath;
		return MyUtils.append(baseURL, "/", StrutsUtils.getSessionLanguage());
	}
	
	public String getLanguage() {
		return StrutsUtils.getSessionLanguage();
	}
	
	public String getBaseRsURL() {
		return getProperty("rs.domain");
	}
	
	public String getResourceImagePath(String filename) {
		String url = getBaseRsURL();

		if (StringUtils.isBlank(filename)) 
			return url + getSiteDefaultImage();

		if (filename.startsWith("/")) {
			url = url + filename;
		} else {
			url = url + "/" + filename;
		}

		return url;
	}

	public String getJsPath(){
//		return getContextPath()+getProperty(PATH_JS, "/js");
		String jspath = getProperty(PATH_JS);
		if(StringUtils.isNotBlank(jspath))
			return getContextPath()+jspath;
		return getBaseLangURL() + "/js";
	}
	
	public String getCssPath(){
		String csspath = getProperty(PATH_CSS);
		if(StringUtils.isNotBlank(csspath))
			return getContextPath()+csspath;
		return getBaseLangURL()+ "/css";
	}
	
	public String getImagePath(){
		String imgpath = getProperty(PATH_IMAGE);
		if(StringUtils.isNotBlank(imgpath))
			return getContextPath()+imgpath;
		return getBaseLangURL()+"/images";
	}

	public String getJqueryPath(){
		return this.getJsPath()+"/jquery";
	}
	
	public String getJqueryuiPath(){
		return this.getJsPath()+"/jqueryui";
	}
	
	public String getDgridPath(){
		return this.getJsPath()+"/dgrid";
	}
	
	public String getDgridLoadingPagefoot(){
		return getDgridPath() + getProperty(DG_LOADING_PAGEFOOT, "/loading_pagefoot.gif");
	}
	
	public String getDgridLoadingCenter(){
		return getDgridPath() + getProperty(DG_LOADING_CENTER, "/loading_center.gif");
	}
	
	public String getTemplateDir(){
		return getProperty("template.dir", false);
	}
	
	public String getTemplateName(){
		return getProperty("template.name", false);
	}
	
	public String getBaseActionPath(){
		return getBaseURL();
	}
	
	public String[] getFilterInitializers(){
		String str = getVariable(FILTER_INITIALIZERS);
		if(StringUtils.isBlank(str))
			return null;
		String[] initers = StringUtils.split(str, ",");
		return initers;
	}

	/***************************************************************************
	 * 返回配置的域名
	 * 
	 * @return
	 */
	public String getDomain() {
		if (StringUtils.isNotBlank(domain))
			return domain;

		domain = getProperty(DOMAIN);
		domain = StrutsUtils.getDomain(domain);

		return domain;
	}

	/***************************************************************************
	 * 为域名加上http://前缀、端口和应用路径
	 * 
	 * @return
	 */
	public String getDomainContext() {
		if (StringUtils.isNotBlank(domainContext))
			return domainContext;

		domainContext = MyUtils.append(StrutsUtils.HTTP_START_KEY, getDomain(),
				getDomainPort().equals("80") ? "" : ":" + getDomainPort(),
				getContextPath());

		return domainContext;
	}

	/**
	 * 比domainContext多了一个语言版本的路径
	 * 
	 * @return
	 */
	public String getWebSite() {
		return MyUtils.append(getDomainContext(), "/", StrutsUtils
				.getSessionLanguage());
	}


	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getDomainPort() {
		String port = getProperty(DOMAIN_PORT);
		if(StringUtils.isBlank(port))
			port = "80";
		return port;
	}


	public String getAppserver() {
		return getProperty(APPSERVER);
	}

	public String getApplicationName() {
		return getProperty(APPLICATION_NAME, "application name");
	}

	public String getUserDetailKey() {
		return getProperty(USER_DETAIL_KEY, UserDetail.USER_DETAIL_KEY);
	}

	public String getTokenName() {
		return getProperty(TOKEN_NAME, UserDetail.TOKEN_KEY);
	}

	public String getSiteDefaultImage(){
		return getProperty(SITE_DEFAULT_IMAGE, "noImage");
	}
	
	public String getLanguageSupport(){ 
		return getProperty(LANGUAGE_SUPPORT);
	}
	
	public boolean isStrutsAutoLanguage(){
		return getBoolean(STRUTS_AOTUOLANGUAGE, true);
	}

	public String getSiteConfigName() {
		return CONFIG_NAME;
	}
}
