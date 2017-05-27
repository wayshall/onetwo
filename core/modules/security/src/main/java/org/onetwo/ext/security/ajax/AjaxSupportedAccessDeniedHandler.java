package org.onetwo.ext.security.ajax;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.mvc.utils.WebResultCreator;
import org.onetwo.common.spring.mvc.utils.WebResultCreator.SimpleResultBuilder;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
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
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
			
	private AccessDeniedHandler delegateAccessDeniedHandler;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	private String redirectErrorUrl;
	private String errorPage;
	
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
		if(RequestUtils.isAjaxRequest(request)){
			SimpleResultBuilder builder = WebResultCreator.creator().error("操作失败："+accessDeniedException.getMessage());
			
			SimpleDataResult<?> rs = WebUtils.buildErrorCode(builder, request, accessDeniedException).buildResult();
			String text = mapper.toJson(rs);
			logger.info("AccessDenied, render json: {}", text);
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else if(!response.isCommitted() && StringUtils.isNotBlank(redirectErrorUrl)) {
			String rurl = redirectErrorUrl;
			if(rurl.contains("?")){
				rurl += "&";
			}else{
				rurl += "?";
			}
			rurl += "accessDenied=true&status="+HttpServletResponse.SC_FORBIDDEN+"&message=";
			rurl += URLEncoder.encode(accessDeniedException.getMessage(), Charsets.UTF_8.name());//encode value, otherwise will redirect failed

			logger.info("AccessDenied, redirect to {}", rurl);
			response.sendRedirect(rurl);
		}else{
			logger.info("AccessDenied, delegateAccessDeniedHandler forward to errorPage: {}", errorPage);
			this.delegateAccessDeniedHandler.handle(request, response, accessDeniedException);
		}
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public void setRedirectErrorUrl(String redirectErrorUrl) {
		this.redirectErrorUrl = redirectErrorUrl;
	}
	

}
