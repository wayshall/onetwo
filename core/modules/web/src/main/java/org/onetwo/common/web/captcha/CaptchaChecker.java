package org.onetwo.common.web.captcha;

import lombok.Data;

/**
 * 无状态的验证码检测器,
 * 暂时使用sha hash实现，有效时间不准确，可换成aes加密实现更准确
 * @author wayshall
 * <br/>
 */
public interface CaptchaChecker {
	boolean check(String code, String hashStr);
	CaptchaSignedResult encode(String code);
	
	@Data
	public static class CaptchaSignedResult {
		private final String signed;
		private final long validTime;
		public CaptchaSignedResult(String signed, long validTime) {
			super();
			this.signed = signed;
			this.validTime = validTime;
		}
	}
}
