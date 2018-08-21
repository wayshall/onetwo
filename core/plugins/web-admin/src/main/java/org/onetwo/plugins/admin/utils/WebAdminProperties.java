package org.onetwo.plugins.admin.utils;

import lombok.Data;

import org.apache.commons.lang3.RandomStringUtils;
import org.onetwo.common.web.captcha.Captchas;
import org.onetwo.common.web.captcha.Captchas.CaptchaChecker;
import org.onetwo.ext.security.provider.CaptchaAuthenticationProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(prefix="jfish.plugin.web-admin")
@Data
public class WebAdminProperties {
	
	CaptchaProps captcha = new CaptchaProps();
	CaptchaChecker captchaChecker;
	
	public CaptchaChecker getCaptchaChecker(){
		if(captchaChecker==null){
			captchaChecker = Captchas.createCaptchaChecker(captcha.getSalt(), captcha.getExpireInSeconds());
		}
		return captchaChecker;
	}

	
	@Data
	public static class CaptchaProps {
		private String salt = RandomStringUtils.randomAscii(64);
		private int expireInSeconds = Captchas.DEFAULT_VALID_IN_SECONDS;
		private String parameterName = CaptchaAuthenticationProvider.PARAMS_VERIFY_CODE;
		private String cookieName = CaptchaAuthenticationProvider.COOKIES_VERIFY_CODE;
	}
}
