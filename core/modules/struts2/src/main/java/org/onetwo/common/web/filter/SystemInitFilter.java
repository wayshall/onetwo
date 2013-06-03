package org.onetwo.common.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.WebException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.CookieUtil;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.StrutsUtils;
import org.onetwo.common.web.utils.Tool;
import org.onetwo.common.web.utils.WebLocaleUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/****
 * 自定义的过滤器
 * @author weishao
 *
 */
@SuppressWarnings("unused")
public class SystemInitFilter extends IgnoreFiler {

	public static final String LOCALE_SESSION_ATTRIBUTE = WebLocaleUtils.ATTRIBUTE_KEY;//I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE;
	
	public static final String REQUEST_ERROR_COUNT = "REQUEST_ERROR_COUNT";

	protected void initApplication(FilterConfig config) {
		logger.info("项目正在启动...");
		super.initApplication(config);
		StrutsUtils.registerConvertor();
		
		ServletContext context = config.getServletContext();

		WebApplicationContext app = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		SpringApplication.initApplication(app);
		
		SiteConfig.getInstance().initWeb(config);
		context.setAttribute(SiteConfig.CONFIG_NAME, SiteConfig.getInstance());
		
	}

	public String[] getFilterInitializers(FilterConfig config){
		return SiteConfig.getInstance().getFilterInitializers();
	}

	public Object getBean(WebApplicationContext app, String beanName) {
		Object bean = null;
		try {
			bean = app.getBean(beanName);
		} catch (Exception e) {
		}
		return bean;
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		try {
			/*if (StringUtils.isBlank(SiteConfig.getInstance().getContextPath())) {
				String contextPath = request.getContextPath();
				SiteConfig.getInstance().setContextPath(contextPath);
			}*/
			this.onFilter(request, response);
			/*String path = request.getRequestURI();
			logger.info("request.getRequestURI(): "+path);
			logger.info("getRequestURI(request): "+getRequestURI(request));
			logger.info("RequestUtils.getServletPath(this.request): "+RequestUtils.getServletPath(request));*/

			super.doFilter(servletRequest, servletResponse, filterChain);
			
			setErrorCount(session, 0);
		}catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e){
		HttpSession session = request.getSession();
		this.logger.error("error: " + e.getMessage(), e);
		e.printStackTrace();
		if(response.isCommitted())
			return ;

		int statusCode = SiteConfig.getInstance().getErrorPageCode();
		if(statusCode!=-1){
			try {
				response.sendError(statusCode);
			} catch (IOException e1) {
				logger.error("send status error: " + statusCode, e1);
			}
			return ;
		}
		
		String errorPage = SiteConfig.getInstance().getErrorPage();
		
		if(getErrorCount(session)>1){
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				pw.write("you did not seem to set the error page. application error : " + e.getMessage());
			} catch (Exception se) {
				se.printStackTrace();
			} finally{
				IOUtils.closeQuietly(pw);
				setErrorCount(session, 0);
			}
			return ;
		}
		
		setErrorCount(session, getErrorCount(session)+1);
		if(StringUtils.isBlank(errorPage)){
			throw new WebException(e);
		}
		String url = getRequestURI(request);
		String path = MyUtils.append(errorPage, "?errorMsg=", Tool.getInstance().encode(e.getMessage()),
				"&url=", Tool.getInstance().encode(url));
		redirect(response, path);
	}

	
	protected int getErrorCount(HttpSession session){
		Integer val = (Integer)session.getAttribute(REQUEST_ERROR_COUNT);
		return val==null?0:val;
	}
	
	protected void setErrorCount(HttpSession session, int count){
		Integer val = (Integer)session.getAttribute(REQUEST_ERROR_COUNT);
		if(val==null)
			val = count;
		else
			val = count;
		session.setAttribute(REQUEST_ERROR_COUNT, val);
	}
		

	public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		
		addP3P(response);
		
		request.setAttribute(StrutsUtils.ACTION_URI_NO_QUERY, request.getRequestURI());
		request.setAttribute(StrutsUtils.ACTION_URI, getRequestURI(request));

		processLocale(request, response);

		this.doProcess(request, response, filterChain);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}
	
	protected void addP3P(HttpServletResponse response){
		response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
	}

	public static String getRequestURI(HttpServletRequest request) {
		return StrutsUtils.getRequestURI(request);
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

		String cookie = request.getParameter("remember_language");
		if ("true".equals(cookie)) {
			CookieUtil.setCookieLanguage(response, closestLocale.toString());
		}
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
	}
}
