package org.onetwo.ext.security.log;


import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.ext.security.metadata.CodeSecurityConfig;
import org.onetwo.ext.security.utils.GenericLoginUserDetails;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*****
 * 去掉了记录输入输出的代码，只在异常时记录异常信息。
 * 实体数据结构先保留。
 * @author way
 *
 */
public class ActionLoggerInterceptor implements HandlerInterceptor, InitializingBean {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String ACTION_REQUEST_KEY = "__ACTION_REQUEST_KEY__";

	@Autowired//(required = false)
//	private FilterSecurityInterceptor filterSecurityInterceptor;
	private SecurityMetadataSource securityMetadataSource;
	
	@Autowired(required=false)
	private ActionLogHandler<AdminActionLog> actionLogHandler;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notNull(securityMetadataSource);
	}
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(shouldLog(handler)){
			try {
				AdminActionLog actionLog = createActionLog(request, response, handler);
				request.setAttribute(ACTION_REQUEST_KEY, actionLog);
			} catch (Exception e) {
				logger.error("createActionLog error: " + e.getMessage(), e);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		AdminActionLog actionLog = (AdminActionLog)request.getAttribute(ACTION_REQUEST_KEY);
		if(actionLog!=null){
			try {
				actionLogHandler.saveLog(actionLog);
			} catch (Exception e) {
				logger.error("saveLog error: " + e.getMessage(), e);
			}
		}
	}
	
	protected boolean shouldLog(Object handler){
		return HandlerMethod.class.isInstance(handler);
	}
	
	private AdminActionLog createActionLog(HttpServletRequest request, HttpServletResponse response, Object handler){
		GenericLoginUserDetails<?> loginUser = SecurityUtils.getCurrentLoginUser();
		AdminActionLog log = new AdminActionLog();
		
		FilterInvocation fi = new FilterInvocation(RequestUtils.getServletPath(request), request.getMethod());
//		Collection<ConfigAttribute> attrs = filterSecurityInterceptor.obtainSecurityMetadataSource().getAttributes(fi);
		Collection<ConfigAttribute> attrs = securityMetadataSource.getAttributes(fi);
		if(attrs!=null){
			attrs.stream()
					.filter(attr->CodeSecurityConfig.class.isInstance(attr))
					.findAny()
					.ifPresent(attr->{
						CodeSecurityConfig codeAttr = (CodeSecurityConfig) attr;
						log.setPermissionCode(codeAttr.getCode());
						log.setPermissionName(codeAttr.getAuthorityName());
					});
		}
		if(loginUser!=null){
			log.setUserId(loginUser.getUserId());
			log.setUserName(loginUser.getUsername());
		}
		log.setActionTime(new Date());
		/*String actionInput = StringUtils.substring(jsonMapper.toJson(request.getParameterMap()), 0, 1000);
		log.setActionInput(actionInput);*/
//		log.setActionOutput(actionOutput);
		log.setActionUrl(RequestUtils.getContextRequestPath(request));
		log.setOperatorIp(RequestUtils.getRemoteAddr(request));
		log.setHttpMethod(request.getMethod());
		log.setIsSuccess(true);
		
		return log;
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		AdminActionLog actionLog = (AdminActionLog)request.getAttribute(ACTION_REQUEST_KEY);
		if(actionLog!=null){
			try {
				if(ex!=null){
					actionLog.setIsSuccess(false);
					actionLog.setActionOutput(StringUtils.substring(ex.getMessage(), 0, 1000));
					actionLogHandler.saveLog(actionLog);
				}
			} catch (Exception e) {
				logger.error("saveLog error: " + e.getMessage(), e);
			}
		}
	}
	
	/*@Override
	public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
		AdminActionLog actionLog = (AdminActionLog)request.getAttribute(ACTION_REQUEST_KEY, RequestAttributes.SCOPE_REQUEST);
		if(actionLog!=null){
			try {
				if(Exception.class.isInstance(concurrentResult)){
					Exception ex = (Exception) concurrentResult;
					actionLog.setIsSuccess(false);
					actionLog.setActionOutput(ex.getMessage());
				}else{
					actionLog.setIsSuccess(true);
					String output = StringUtils.substring(jsonMapper.toJson(concurrentResult), 0, 1000);
					actionLog.setActionOutput(output);
				}
				actionLogHandler.saveLog(actionLog);
			} catch (Exception e) {
				logger.error("saveLog error: " + e.getMessage(), e);
			}
		}
	}*/


}
