package org.onetwo.ext.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator.SimpleResultBuilder;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

public class AjaxSupportedAccessDeniedHandler implements AccessDeniedHandler {
	
	private AccessDeniedHandler delegateAccessDeniedHandler;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	public AjaxSupportedAccessDeniedHandler(){
		delegateAccessDeniedHandler = new AccessDeniedHandlerImpl();
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
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else{
			this.delegateAccessDeniedHandler.handle(request, response, accessDeniedException);
		}
	}
	

}
