package org.onetwo.boot.module.oauth2.ssoclient.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

/**
 * OAuth2ClientAuthenticationProcessingFilter非 BadCredentialsException 异常直接抛出时，
 * 前端无法正常接收到json数据返回。
 * 这里增加对 ServletException 异常处理，包装成 BadCredentialsException 异常
 * @author weishao zeng
 * <br/>
 */
public class ExtOAuth2ClientAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

	public ExtOAuth2ClientAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		try {
			return super.attemptAuthentication(request, response);
		} catch (BadCredentialsException e) {
			throw e;
		} catch (ServletException | BaseException e) {
			AuthenticationException ae = new InternalAuthenticationServiceException("Could not obtain user: " + e.getMessage(), e);
			throw ae;
		}
	}
}
