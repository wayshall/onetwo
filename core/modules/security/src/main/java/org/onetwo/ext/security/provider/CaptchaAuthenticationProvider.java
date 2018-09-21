package org.onetwo.ext.security.provider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.web.captcha.CaptchaChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CaptchaAuthenticationProvider implements AuthenticationProvider {
	public static final String PARAMS_VERIFY_CODE = "verifyCode";
	public static final String COOKIES_VERIFY_CODE = "_cv";

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	private CaptchaChecker captchaChecker;
	private String captchaParameterName = PARAMS_VERIFY_CODE;
	private String captchaCookieName = COOKIES_VERIFY_CODE;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String validCode = getVerifyCode();
		if(StringUtils.isBlank(validCode)){
			throw new InternalAuthenticationServiceException("请填写验证码！");
		}
		try {
			check(validCode);
		} catch (AuthenticationException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalAuthenticationServiceException("check verify code error", e);
		}
		return null;
	}

	protected void check(String validCode){
		String captchaValue = getCookieValue(request, captchaCookieName);
		if(!captchaChecker.check(validCode.toLowerCase(), captchaValue)){
			throw new InternalAuthenticationServiceException("验证码失效或错误！");
		}
		this.removeCookie();
	}
	
	protected String getVerifyCode(){
		return request.getParameter(captchaParameterName);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					return (cookie.getValue());
				}
			}
		}
		return null;
	}
	
	public void removeCookie(){
		Cookie cookie = new Cookie(captchaCookieName, null);
		cookie.setMaxAge(0);
		cookie.setSecure(request.isSecure());
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}

	public void setCaptchaParameterName(String captchaParameterName) {
		this.captchaParameterName = captchaParameterName;
	}

	public void setCaptchaCookieName(String captchaCookieName) {
		this.captchaCookieName = captchaCookieName;
	}

	public void setCaptchaChecker(CaptchaChecker captchaChecker) {
		this.captchaChecker = captchaChecker;
	}
	
}
