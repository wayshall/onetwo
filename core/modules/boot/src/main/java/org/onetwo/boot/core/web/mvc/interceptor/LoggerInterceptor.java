package org.onetwo.boot.core.web.mvc.interceptor;

import java.util.Date;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.mvc.exception.ExceptionMessageFinder.ErrorMessage;
import org.onetwo.boot.core.web.mvc.log.AccessLogProperties;
import org.onetwo.boot.core.web.mvc.log.OperationLogs;
import org.onetwo.boot.core.web.mvc.log.OperatorLogEvent;
import org.onetwo.boot.core.web.mvc.log.OperatorLogInfo;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.ds.ContextHolder;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.utils.JFishMathcer;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;

public class LoggerInterceptor extends WebInterceptorAdapter implements InitializingBean {
//	public static final int INTERCEPTOR_ORDER = BootFirstInterceptor.INTERCEPTOR_ORDER+100;

	public static interface UserDetailRetriever {
		UserDetail getUserDetail();
	}
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	private JsonMapper jsonMapper = JsonMapper.mapper(Inclusion.NON_NULL, true);
	@Autowired(required=false)
	private ContextHolder contextHolder;
//	private AccessLogger accessLogger;
	private final boolean logOperation = true;
	/***
	 * 用于记录日志时，避免记录一些敏感参数
	 */
	private JFishMathcer parameterMatcher ;
	private String[] excludes;
	private UserDetailRetriever userDetailRetriever;
//	@Autowired
//	private BootSiteConfig bootSiteConfig;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private AccessLogProperties accessLogProperties;
	
	public LoggerInterceptor(){
	}
	
	public void afterPropertiesSet() throws Exception{
		if(LangUtils.isEmpty(excludes)){
			this.excludes = new String[]{"*password*"};
		}
		this.parameterMatcher = JFishMathcer.excludes(false, excludes);
		/*if(isLogOperation() && accessLogger==null){
			DefaultAccessLogger defaultLogger = new DefaultAccessLogger();
//			if(bootSiteConfig!=null)
			defaultLogger.setLoggerName(accessLogProperties.getLoggerName());
			defaultLogger.setSeprator(accessLogProperties.getSeprator());
			defaultLogger.setLogChangedDatas(accessLogProperties.isLogChangedDatas());
			defaultLogger.setLogControllerDatas(accessLogProperties.isLogControllerDatas());
			defaultLogger.initLogger();
			this.accessLogger = defaultLogger;
		}*/
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		request.setAttribute(START_TIME_KEY, System.currentTimeMillis());;
//		WebContextUtils.initRequestInfo(request);
		OperationLogs.initLogInfo(request);
		return true;
	}
	
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if(!isLogOperation())
			return ;
		
		try {
			log(request, response, handler, ex);
		} catch (Exception e) {
			logger.error("log error: {}", e.getMessage(), e);
		}
	}
	
	
	public void log(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		/*AuthenticationContext authen = AuthenticUtils.getContextFromRequest(request);
		if(authen==null)
			return ;*/
		if(handler==null || !HandlerMethod.class.isInstance(handler))
			return ;

//		RequestInfo reqInfo = WebContextUtils.requestInfo(request);
		OperatorLogInfo reqInfo = OperationLogs.getLogInfo(request);
		if(reqInfo==null)
			return ;

//		String url = request.getMethod() + "|" + request.getRequestURL();
//		long start = reqInfo.getStartTime();
//		OperatorLogInfo info = new OperatorLogInfo(start, System.currentTimeMillis());
//		info.setUrl(url);
		OperatorLogInfo info = reqInfo;
		
		info.setRemoteAddr(RequestUtils.getRemoteAddr(request));
		info.setUserAgent(RequestUtils.getUserAgent(request));
//		info.setParameters(request.getParameterMap());
		for(Entry<String, String[]> entry : request.getParameterMap().entrySet()){
			/*try {
				if(matcher.match(entry.getKey())){
					info.addParameter(entry.getKey(), entry.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			if(parameterMatcher.match(entry.getKey())){
				String[] values = Stream.of(entry.getValue()).map(val -> {
					return StringUtils.substring(val, 0, this.accessLogProperties.getLogParameterValueMaxLength());
				}).collect(Collectors.toList()).toArray(new String[0]);
				info.addParameter(entry.getKey(), values);
			}else{
				info.addParameter(entry.getKey(), "******");
			}
		}
		

//		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetail userdetail = userDetailRetriever!=null?userDetailRetriever.getUserDetail():null;//JFishWebUtils.getUserDetail();
		if(userdetail!=null){
			info.setOperatorId(userdetail.getUserId());
			info.setOperatorName(userdetail.getUserName());
		}
		ErrorMessage errorMessage = BootWebUtils.webHelper(request).getErrorMessage();
		if(ex!=null){
			info.setSuccess(false);
			info.setMessage(ex.getMessage());
		}else if(errorMessage!=null){
			info.setSuccess(false);
			info.setMessage(errorMessage.getMesage());
		}
		info.setOperatorTime(new Date());
		if(contextHolder!=null){
			info.setDatas(contextHolder.getDataChangedContext());
		}
		info.setRequestMethod(request.getMethod());
		
		HandlerMethod webHandler = (HandlerMethod)handler;
		info.setHandlerMethod(webHandler);
		info.setWebHandler(webHandler.getBeanType().getCanonicalName()+"."+webHandler.getMethod().getName());
		
//		accessLogger.logOperation(info);
		OperatorLogEvent event = new OperatorLogEvent(this, info);
		applicationContext.publishEvent(event);
	}

	@Override
	public int getOrder() {
		return ORDERED_LOG;
	}

	public void setContextHolder(ContextHolder contextHolder) {
		this.contextHolder = contextHolder;
	}
	/*public void setAccessLogger(AccessLogger accessLogger) {
		this.accessLogger = accessLogger;
	}*/
	public boolean isLogOperation() {
		return logOperation;
	}

	public void setUserDetailRetriever(UserDetailRetriever userDetailRetriever) {
		this.userDetailRetriever = userDetailRetriever;
	}
	
	
}
