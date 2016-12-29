package org.onetwo.common.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.PostfixMatcher;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.WebContextUtils;
import org.slf4j.Logger;


public abstract class IgnoreFiler implements Filter{

	public static final String EXCLUDE_SUFFIXS_NAME = "excludeSuffixs";

	public static final String INCLUDE_SUFFIXS_NAME = "includeSuffixs";

	private PostfixMatcher postfixMatcher;
	
	/*****
	 * 是否过滤后缀
	 */
	protected boolean filterSuffix = false;

	protected List<WebFilterInitializers> filterInitializers;
	protected List<WebFilter> webFilters;
	

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final public void init(FilterConfig config) throws ServletException {
//		logger = this.initLogger(config);
//		logger.info("项目正在启动...");
		this.onFilterInitialize(config);
		
		filterSuffix = "true".equals(config.getInitParameter("filterSuffix"));
		if(filterSuffix){
			this.postfixMatcher = new PostfixMatcher(config.getInitParameter("excludeSuffixs"), config.getInitParameter("includeSuffixs"));
		}

		initWebFilterInitializers(config);
		
	}
	
	/*too late
	 * protected Logger initLogger(FilterConfig config) throws ServletException {
		Logger logger = MyLoggerFactory.getLogger(this.getClass());
		return logger;
	}*/
	
	protected void onFilterInitialize(FilterConfig config) {
		/*ServletContext context = config.getServletContext();
		
		WebApplicationContext app = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		SpringApplication.getInstance().initApplication(app);*/
	}
	

/*	public String[] getWebFilters(FilterConfig config){
		return null;
	}*/
	
	protected void initWebFilterInitializers(FilterConfig config){
		filterInitializers = (List<WebFilterInitializers>) SpringApplication.getInstance().getBeans(WebFilterInitializers.class);
		webFilters = (List<WebFilter>) SpringApplication.getInstance().getBeans(WebFilter.class);
		
		/*if(filterInitializers==null || filterInitializers.isEmpty())
			return ;*/

		/*
		Collections.sort(filterInitializers, new Comparator<FilterInitializer>(){
			@Override
			public int compare(FilterInitializer o1, FilterInitializer o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});*/
		
		for(WebFilterInitializers filterIniter : filterInitializers){
			filterIniter.onInit(config);
		}
	}
	

	protected void onFilter(HttpServletRequest request, HttpServletResponse response){
		if(webFilters==null || webFilters.isEmpty())
			return ;
		for(WebFilter webfilter : webFilters){
			webfilter.onFilter(request, response);
		}
	}


	/*protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException, ServletException{
		if(webFilters==null || webFilters.isEmpty())
			return ;
		for(WebFilter webfilter : webFilters){
			webfilter.onThrowable(request, response, ex);
		}
	}*/

	protected void onFinally(HttpServletRequest request, HttpServletResponse response){
		if(webFilters==null || webFilters.isEmpty())
			return ;
		for(WebFilter webfilter : webFilters){
			webfilter.onFinally(request, response);
		}
	}
	
	protected HttpServletRequest wrapRequest(ServletRequest servletRequest){
		return (HttpServletRequest) servletRequest;
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = wrapRequest(servletRequest);
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		WebContextUtils.initRequestInfo(request);

		/*if(!filterSuffix){
			filterChain.doFilter(request, response);
			return ;
		}*/
		
		try {
			this.onFilter(request, response);
			if (this.requestSupportFilter(request)) {
				this.doFilterInternal(request, servletResponse, filterChain);
			}else {
				filterChain.doFilter(request, response);
			}
		}finally {
			this.onFinally(request, response);
		}
	}

	abstract public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException ;


	/***
	 * 请求是否支持过滤器
	 * 没有配置的默认不需要过滤器链
	 * @param request
	 * @return 返回false 表示请求不需要经过过滤器，返回true 表示请求需要经过过滤器链
	 * @throws ServletException
	 */
	protected boolean requestSupportFilter(HttpServletRequest request) throws ServletException {
		if(!filterSuffix){//如果设置了不要过滤后缀,必须经过过滤器
			return true;
		}
		String uri = RequestUtils.getServletPath(request);

		if(!uri.contains(".")){//没有后缀的url
			return true;
		}
		
		return postfixMatcher.isMatch(uri);
	}
	
	public void destroy(){
	}
}
