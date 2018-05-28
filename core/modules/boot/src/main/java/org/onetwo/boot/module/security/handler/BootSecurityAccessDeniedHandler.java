package org.onetwo.boot.module.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.view.MvcViewRender;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wayshall
 * <br/>
 */
public class BootSecurityAccessDeniedHandler extends AjaxSupportedAccessDeniedHandler {
	
	@Autowired(required=false)
	private MvcViewRender mvcViewRender;

	
	protected void defaultHandle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException{
		if(mvcViewRender==null){
			super.defaultHandle(request, response, accessDeniedException);
			return ;
		}
		String url = request.getMethod() + "|" + request.getRequestURI();
		logger.info("{} AccessDenied, render errorPage: {}", url, errorPage);
		ModelAndView mv = new ModelAndView(errorPage);
		mv.addObject("exception", accessDeniedException);
		mv.addObject("errorMessage", getErrorMessage(accessDeniedException));
		this.mvcViewRender.render(mv, request, response);
	}
}
