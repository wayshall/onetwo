package org.onetwo.ext.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/****
 * 验证入口点
 * 当然filter检测到没有登录时，调用commence方法
 * @author way
 *
 */
public class AjaxSupportedAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

	private AuthenticationEntryPoint defaultAuthenticationEntryPoint;
	@Autowired
	private SecurityConfig securityConfig;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(defaultAuthenticationEntryPoint==null)
			defaultAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(securityConfig.getLoginUrl());
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if(RequestUtils.isAjaxRequest(request)){
			SimpleDataResult<?> rs = WebResultCreator.creator()
													.error(authException.getMessage())
													.buildResult();
			String text = mapper.toJson(rs);
			ResponseUtils.renderJsonByAgent(request, response, text);
		}else{
			this.defaultAuthenticationEntryPoint.commence(request, response, authException);
		}
	}

	public void setDefaultAuthenticationEntryPoint(AuthenticationEntryPoint defaultAuthenticationEntryPoint) {
		this.defaultAuthenticationEntryPoint = defaultAuthenticationEntryPoint;
	}

}
