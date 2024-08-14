package org.onetwo.boot.captcha;

import org.onetwo.common.web.captcha.Captchas;
import org.onetwo.common.web.captcha.SimpleCaptchaGenerator.CaptchaSettings;
import org.onetwo.ext.security.provider.CaptchaAuthenticationProvider;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class CaptchaProps {
	
	private String salt;
	/**
	 * 与salt同义 
	 */
	private String key;
	private int expireInSeconds = Captchas.DEFAULT_VALID_IN_SECONDS;
	private String parameterName = CaptchaAuthenticationProvider.PARAMS_VERIFY_CODE;
	private String cookieName = CaptchaAuthenticationProvider.COOKIES_VERIFY_CODE;
	private CaptchaCoder coder = CaptchaCoder.SHA;
	
	CaptchaSettings settings = new CaptchaSettings();

	public void setSalt(String salt) {
		this.salt = salt;
		this.key = salt;
	}

	public void setKey(String key) {
		this.key = key;
		this.salt = key;
	}

}
