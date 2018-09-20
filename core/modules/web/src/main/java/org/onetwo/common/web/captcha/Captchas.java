package org.onetwo.common.web.captcha;

import org.apache.commons.lang3.RandomStringUtils;
import org.onetwo.common.web.captcha.CaptchaChecker.CaptchaSignedResult;
import org.onetwo.common.web.captcha.CaptchaChecker.HashCaptchaChecker;

/**
 * @author wayshall
 * <br/>
 */
public class Captchas {
//	static public final String DEFAULT_SALT = "__jfish_captchas__";
	/**
	 * 默认失效时间，三分钟
	 */
	static public final int DEFAULT_VALID_IN_SECONDS = 60*3;
	
	static private final CaptchaChecker CAPTCHA_CHECKER = new HashCaptchaChecker(RandomStringUtils.randomAscii(64), DEFAULT_VALID_IN_SECONDS);

	
	public static CaptchaChecker getCaptchaChecker() {
		return CAPTCHA_CHECKER;
	}
	
	public static CaptchaChecker createCaptchaChecker(String salt, int validInSeconds) {
		return new HashCaptchaChecker(salt, validInSeconds);
	}

	public static CaptchaSignedResult signCode(String code){
		return CAPTCHA_CHECKER.encode(code);
	}
	
	public static boolean checkCode(String code, String hashStr){
		return CAPTCHA_CHECKER.check(code, hashStr);
	}
	
	
	private Captchas(){
	}

}
