package org.onetwo.plugins.admin.utils;

import lombok.Data;

import org.onetwo.common.web.captcha.Captchas;
import org.onetwo.common.web.captcha.Captchas.CaptchaChecker;
import org.onetwo.ext.security.provider.CaptchaAuthenticationProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(prefix=WebAdminProperties.PREFIX)
@Data
public class WebAdminProperties {
	
	public static final String PREFIX = "jfish.plugin.web-admin";
	
	CaptchaProps captcha = new CaptchaProps();
	CaptchaChecker captchaChecker;
	
	public CaptchaChecker getCaptchaChecker(){
		Assert.hasText(captcha.getSalt(), "property[jfish.plugin.web-admin.captcha.salt] must has text!");
		if(captchaChecker==null){
			captchaChecker = Captchas.createCaptchaChecker(captcha.getSalt(), captcha.getExpireInSeconds());
		}
		return captchaChecker;
	}

	
	@Data
	public static class CaptchaProps {
		public static final String ENABLED_KEY = PREFIX+".captcha.enabled";
		
		private String salt;
		private int expireInSeconds = Captchas.DEFAULT_VALID_IN_SECONDS;
		private String parameterName = CaptchaAuthenticationProvider.PARAMS_VERIFY_CODE;
		private String cookieName = CaptchaAuthenticationProvider.COOKIES_VERIFY_CODE;
		
		private String color;
	}
}
