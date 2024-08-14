package org.onetwo.boot.captcha;

import org.onetwo.common.spring.Springs;
import org.onetwo.common.web.captcha.AESCaptchaChecker;
import org.onetwo.common.web.captcha.CaptchaChecker;
import org.onetwo.common.web.captcha.Captchas;

/**
 * @author weishao zeng
 * <br/>
 */
public enum CaptchaCoder {

	SHA {
		@Override
		public CaptchaChecker createChecker(CaptchaProps props) {
			return Captchas.createCaptchaChecker(props.getSalt(), props.getExpireInSeconds());
		}
	},
	
	AES {
		@Override
		public CaptchaChecker createChecker(CaptchaProps props) {
			return new AESCaptchaChecker(props.getSalt(), props.getExpireInSeconds());
		}
	},
	
	REDIS {
		@Override
		public CaptchaChecker createChecker(CaptchaProps props) {
			RedisCaptchaChecker checker = new RedisCaptchaChecker(props.getExpireInSeconds());
			Springs.getInstance().autoInject(checker);
			return checker;
		}
	};
	
	abstract public CaptchaChecker createChecker(CaptchaProps props);
}
