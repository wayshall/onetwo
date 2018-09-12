package org.onetwo.ext.security.ajax;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.spring.mvc.utils.DataResults.SimpleResultBuilder;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebUtils;
import org.onetwo.ext.security.SecurityExceptionMessager;
import org.onetwo.ext.security.utils.SecurityUtils.SecurityErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.google.common.base.Charsets;

/****
 * 访问拒绝处理器，没有权限时调用此处理器
 * @author way
 *
 */
public class AjaxSupportedAccessDeniedHandler implements AccessDeniedHandler, InitializingBean {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
			
	protected AccessDeniedHandler delegateAccessDeniedHandler;
	protected JsonMapper mapper = JsonMapper.IGNORE_NULL;
	protected String redirectErrorUrl;
	protected String errorPage;
	@Autowired(required=false)
	private SecurityExceptionMessager securityExceptionMessager;
	
	public AjaxSupportedAccessDeniedHandler(){
		delegateAccessDeniedHandler = new AccessDeniedHandlerImpl();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AccessDeniedHandlerImpl adh = new AccessDeniedHandlerImpl();
		adh.setErrorPage(errorPage);
		this.delegateAccessDeniedHandler = adh;
	}
	
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		String url = request.getMethod() + "|" + request.getRequestURI();
		String errorMsg = getErrorMessage(accessDeniedException);
		
		if(RequestUtils.isAjaxRequest(request)){
			SimpleResultBuilder<?> builder = DataResults.error(errorMsg+
																	", at "+request.getRequestURI())
															.code(SecurityErrors.ACCESS_DENIED)
															.data(url);
			
			DataResult<?> rs = WebUtils.buildErrorCode(builder, request, accessDeniedException).build();
			String text = mapper.toJson(rs);
			logger.info("[] AccessDenied, render json: {}", url, text);
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else if(!response.isCommitted() && StringUtils.isNotBlank(redirectErrorUrl)) {
			String rurl = redirectErrorUrl;
			if(rurl.contains("?")){
				rurl += "&";
			}else{
				rurl += "?";
			}
			rurl += "accessDenied=true&status="+HttpServletResponse.SC_FORBIDDEN+"&message=";
			rurl += URLEncoder.encode(errorMsg, Charsets.UTF_8.name());//encode value, otherwise will redirect failed

			logger.info("{} AccessDenied, redirect to {}", url, rurl);
			response.sendRedirect(rurl);
		}else{
			defaultHandle(request, response, accessDeniedException);
		}
	}
	
	protected void defaultHandle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException{
		String url = request.getMethod() + "|" + request.getRequestURI();
		logger.info("{} AccessDenied, delegateAccessDeniedHandler forward to errorPage: {}", url, errorPage);
		this.delegateAccessDeniedHandler.handle(request, response, accessDeniedException);
	}
	
	final protected String getErrorMessage(AccessDeniedException accessDeniedException){
		String errorMsg = accessDeniedException.getMessage();
		if(securityExceptionMessager!=null){
			errorMsg = securityExceptionMessager.findMessageByThrowable(accessDeniedException);
		}
		return errorMsg;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public void setRedirectErrorUrl(String redirectErrorUrl) {
		this.redirectErrorUrl = redirectErrorUrl;
	}
	

}
