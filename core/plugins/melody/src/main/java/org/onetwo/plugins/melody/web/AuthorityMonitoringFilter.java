package org.onetwo.plugins.melody.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bull.javamelody.MonitoringFilter;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.ResponseUtils;

public class AuthorityMonitoringFilter extends MonitoringFilter {
	
	public AuthorityMonitoringFilter(){
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest.getRequestURI().equals(getMonitoringUrl(httpRequest))){
			UserDetail userDetail = JFishWebUtils.getUserDetail();
			if(userDetail==null || !userDetail.isSystemRootUser()){
				ResponseUtils.renderText(httpResponse, "没有权限!");
				return ;
			}
		}
		super.doFilter(request, response, chain);
	}

}
