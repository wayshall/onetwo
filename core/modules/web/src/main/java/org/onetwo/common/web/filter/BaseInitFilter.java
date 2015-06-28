package org.onetwo.common.web.filter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebLocaleUtils;
import org.onetwo.common.web.xss.XssPreventRequestWrapper;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/****
 * 自定义的过滤器
 * @author weishao
 *
 */
@SuppressWarnings("unused")
public class BaseInitFilter extends IgnoreFiler {
	public static final String SITE_CONFIG_NAME = "siteConfig";
	public static final String WEB_CONFIG_NAME = "webConfig";
	
	public static final String PREVENT_XSS_REQUEST = "prevent.xss.request";//xss
	public static final String COOKIE_P3P = "cookie.p3p";
	public static final String TIME_PROFILER = "time.profiler";

//	public static final String START_TIME_KEY = "org.onetwo.logger.request.startTime";
//	public static final String END_TIME_KEY = "org.onetwo.logger.request.endTime";
	

	public static final String LOCALE_SESSION_ATTRIBUTE = WebLocaleUtils.ATTRIBUTE_KEY;//I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE;

	public static final String RELOAD = "reload";
	public static final String JNA_LIBRARY_PATH = "jna.library.path";
	
//	public static final String REQUEST_ERROR_COUNT = "REQUEST_ERROR_COUNT";
	public static final String LANGUAGE = "cookie.language";
	public static final String REQUEST_URI = RequestUtils.REQUEST_URI;
	private boolean timeProfiler = false;//BaseSiteConfig.getInstance().isTimeProfiler();
	
	private boolean preventXssRequest = false;
	private AppConfig siteConfig;

	protected void initApplication(FilterConfig config) {
		
		super.initApplication(config);
		
		String libraryPath = System.getProperty(JNA_LIBRARY_PATH);
		logger.info("jna.library.path: {}", libraryPath);
		
		ServletContext context = config.getServletContext();
		
		WebApplicationContext app = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
//		SpringApplication.initApplication(app);
		SpringApplication.initApplicationIfNotInitialized(app);
		
		AppConfigProvider webConfigProvider = SpringUtils.getBean(app, AppConfigProvider.class);
		if(webConfigProvider!=null){
			siteConfig = webConfigProvider.createAppConfig(config);
			Assert.notNull(siteConfig);
			context.setAttribute(SITE_CONFIG_NAME, siteConfig);
//			context.setAttribute(WEB_CONFIG_NAME, webConfigProvider.createWebConfig(config));
			logger.info("find webConfigProvider : {}", webConfigProvider);
			
			this.initOnAppConfig(siteConfig);
		}else{
			siteConfig = AppConfig.create(true);
			logger.info("no webConfigProvider found.");
		}
		
		List<WebContextConfigProvider> configs = SpringUtils.getBeans(app, WebContextConfigProvider.class);
		configs.stream().forEach(cnf->{
			context.setAttribute(cnf.getConfigName(), cnf.getWebConfig(config));
		});
		
		this.initWithWebApplicationContext(config, app);
	}
	
	protected void setPreventXssRequest(boolean preventXssRequest) {
		this.preventXssRequest = preventXssRequest;
	}

	protected void initWithWebApplicationContext(FilterConfig config, WebApplicationContext app){
	}
	
	protected void initOnAppConfig(AppConfig appConfig){
		//xss
		this.preventXssRequest = appConfig.getBoolean(PREVENT_XSS_REQUEST, false);
		UtilTimerStack.active(appConfig.getBoolean(TIME_PROFILER, appConfig.isDev()));
	}

	/*public String[] getWebFilters(FilterConfig config){
		return getBaseSiteConfig().getFilterInitializers();
	}*/

	protected HttpServletRequest wrapRequest(ServletRequest servletRequest){
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if(preventXssRequest){
			request = new XssPreventRequestWrapper(request);
		}
		return request;
	}
	
	protected void printRequestTime(boolean push, HttpServletRequest request){
		if(!timeProfiler)
			return ;
		String url = request.getMethod() + "|" + request.getRequestURI();
		if(push){
			UtilTimerStack.push(url);
		}else{
			UtilTimerStack.pop(url);
		}
	}
	
	protected void reloadConfigIfNecessary(HttpServletRequest request){
		boolean reloadSiteConfig = RELOAD.equals(request.getParameter(SITE_CONFIG_NAME));
		if(reloadSiteConfig){
			siteConfig.reload();
			this.initOnAppConfig(siteConfig);
		}
	}


	public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		

//		WebContextUtils.initRequestInfo(request);
		this.printRequestTime(true, request);
		request.setAttribute(REQUEST_URI, RequestUtils.getServletPath(request));
		try {
			this.reloadConfigIfNecessary(request);
			if(siteConfig.getBoolean(COOKIE_P3P, false))
				addP3P(response);
			processLocale(request, response);
			
//			ResponseUtils.setHttpOnlyCookie(response, "aa2", "bb2", "/", 5, ".a.com");
//			ResponseUtils.setHttpOnlyCookie(response, "aa1", "bb1", "/", 5, ".b.com");

			filterChain.doFilter(request, response);
		}catch (ServletException e) {
			this.logger.error("request["+getRequestURI(request)+"] error: " + e.getMessage(), e);
//			handleException(request, response, e);
			throw e;
		}catch (Exception e) {
			this.logger.error("request["+getRequestURI(request)+"] error: " + e.getMessage(), e);
			throw e;
//			handleException(request, response, e);
		} finally{
			this.printRequestTime(false, request);
		}
		
	}

	/*protected void doProcess(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}*/
	
	protected void addP3P(HttpServletResponse response){
		ResponseUtils.addP3PHeader(response);
	}

	public static String getRequestURI(HttpServletRequest request) {
		return request.getRequestURI();
	}

	/***
	 * 处理request的locale，用于国际化
	 * @param request
	 * @param response
	 */
	protected void processLocale(HttpServletRequest request, HttpServletResponse response) {
//		StrutsUtils.setCurrentSessioniLocale(request.getSession(), BusinessLocale.getDefault());
		/*
		 * Locale locale = StrutsUtils.getCurrentSessionLocale(request, null);
		 * if(locale==null){
		 * StrutsUtils.setCurrentSessioniLocale(request.getSession(),BusinessLocale.getDefault()); }
		 * processLocaleByPath(request, response);
		 */
		// processDataLocale(request);
	}

	protected void processDataLocale(HttpServletRequest request) {
		String localeStr = request.getParameter(WebLocaleUtils.DATA_LOCALE_ATTRIBUTE_KEY);
		Locale locale = WebLocaleUtils.getClosestLocale(localeStr);
		request.getSession().setAttribute(WebLocaleUtils.DATA_LOCALE_ATTRIBUTE_KEY, locale);
	}

	/***
	 * 根据访问路径中的语言路径，设置请求的locale，用于国际化
	 * @param request
	 * @param response
	 */
	protected void processLocaleByPath(HttpServletRequest request, HttpServletResponse response) {
		String uri = RequestUtils.getServletPath(request);

		String localeStr = "";
		int index = uri.indexOf('/', 1);
		if (index != -1)
			localeStr = uri.substring(1, index);

		Locale currentLocale = (Locale) request.getSession().getAttribute(WebLocaleUtils.ATTRIBUTE_KEY);
		Locale closestLocale = null;
		if (WebLocaleUtils.isSupport(localeStr)) {
			closestLocale = WebLocaleUtils.getClosestLocale(localeStr);
		} else
			closestLocale = WebLocaleUtils.getClosestLocale(currentLocale.toString());

		if (closestLocale != null && !WebLocaleUtils.isSameLocale(currentLocale, closestLocale)) {
			request.getSession().setAttribute(WebLocaleUtils.ATTRIBUTE_KEY, closestLocale);
		}

		/*String cookie = request.getParameter("remember_language");
		if ("true".equals(cookie)) {
			CookieUti.setCookieLanguage(response, closestLocale.toString());
		}*/
	}

	/****
	 * 重定向
	 * @param response
	 * @param path
	 */
	public static void redirect(HttpServletResponse response, String path) {
		try {
			response.sendRedirect(path);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/***
	 * 转发
	 * @param request
	 * @param response
	 * @param path
	 */
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path) {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		try {
			rd.forward(request, response);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void destroy() {
		super.destroy();
	}
}
