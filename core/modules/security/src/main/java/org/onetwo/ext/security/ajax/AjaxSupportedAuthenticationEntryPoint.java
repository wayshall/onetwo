package org.onetwo.ext.security.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.data.DataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityUtils.SecurityErrors;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortMapperImpl;
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

	private boolean forceHttps;
	private Integer httpsPort;
	private boolean contextRelative;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(defaultAuthenticationEntryPoint==null){
			LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint(securityConfig.getLoginUrl());
			entryPoint.setForceHttps(forceHttps);
			entryPoint.setPortMapper(new PortMapperImpl(){
				public Integer lookupHttpsPort(Integer httpPort) {
					Integer port = super.lookupHttpsPort(httpPort);
					return port==null?httpsPort:port;
				}
			});
			PropertyAccessorFactory.forDirectFieldAccess(entryPoint).setPropertyValue("redirectStrategy.contextRelative", contextRelative);
			this.defaultAuthenticationEntryPoint = entryPoint;
		}
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if(RequestUtils.isAjaxRequest(request)){
			DataResult<?> rs = DataResults.error(authException.getMessage())
											.code(SecurityErrors.NOT_TRUSTED)
											.build();
			String text = mapper.toJson(rs);
			ResponseUtils.renderJsonByAgent(request, response, text);
		}else{
			this.defaultAuthenticationEntryPoint.commence(request, response, authException);
		}
	}

	public void setDefaultAuthenticationEntryPoint(AuthenticationEntryPoint defaultAuthenticationEntryPoint) {
		this.defaultAuthenticationEntryPoint = defaultAuthenticationEntryPoint;
	}

	public void setForceHttps(boolean forceHttps) {
		this.forceHttps = forceHttps;
	}

	public void setHttpsPort(Integer httpsPort) {
		this.httpsPort = httpsPort;
	}

	public void setContextRelative(boolean contextRelative) {
		this.contextRelative = contextRelative;
	}

}
