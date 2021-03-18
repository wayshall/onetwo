package org.onetwo.boot.module.oauth2.ssoclient.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.module.oauth2.ssoclient.event.AccessTokenObtainedEvent;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * OAuth2ClientAuthenticationProcessingFilter非 BadCredentialsException 异常直接抛出时，
 * 前端无法正常接收到json数据返回。
 * 这里增加对 ServletException 异常处理，包装成 BadCredentialsException 异常
 * @author weishao zeng
 * <br/>
 */
public class ExtOAuth2ClientAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

	private ApplicationEventPublisher eventPublisher;
	
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

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		OAuth2AccessToken accessToken = restTemplate.getAccessToken();
		UserDetail userDetail = (UserDetail)authResult.getPrincipal();
		AccessTokenObtainedEvent event = new AccessTokenObtainedEvent(this, userDetail, accessToken);
		this.eventPublisher.publishEvent(event);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		super.setApplicationEventPublisher(eventPublisher);
	}
}
