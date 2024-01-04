package org.onetwo.ext.security.ajax;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.common.data.DataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.ext.security.exception.ErrorMessageExtractor;
import org.onetwo.ext.security.exception.SecurityErrorResult;
import org.onetwo.ext.security.exception.SecurityErrors;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/****
 * 验证入口点
 * 当ExceptionTranslationFilter检测到登录异常（AuthenticationException）时，调用commence方法，跳转到登录入口
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
//	@Autowired
	private ErrorMessageExtractor errorMessageExtractor;
	@Autowired
	private ApplicationContext applicationContext;
	
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
		this.errorMessageExtractor =  SpringUtils.getBean(applicationContext, ErrorMessageExtractor.class);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if(RequestUtils.isAjaxRequest(request)){
			SecurityErrorResult result = errorMessageExtractor.getErrorMessage(authException);
			DataResult<?> rs = null;
			if (result.isUnknowError()) {
				rs = DataResults.error(SecurityErrors.CM_NOT_LOGIN.getErrorMessage())
												.code(SecurityErrors.CM_NOT_LOGIN)
												.build();
			} else {
				rs = DataResults.error(result.getMessage())
												.code(result.getCode())
												.build();
			}
			errorMessageExtractor.handleErrorResponse(response, rs);
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
