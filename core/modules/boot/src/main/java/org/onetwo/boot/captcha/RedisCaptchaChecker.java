package org.onetwo.boot.captcha;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.captcha.CaptchaChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */
public class RedisCaptchaChecker implements CaptchaChecker {
	final private long expireInSeconds;
	
	@Autowired
	private RedisOperationService redisOperationService;

	public RedisCaptchaChecker(int validInSeconds) {
		super();
		Assert.isTrue(validInSeconds>0, "validInSeconds["+validInSeconds+"] must greater than 0");
		this.expireInSeconds = validInSeconds;
	}
	
	public boolean check(String code, String redisKey){
		String key = getCaptchaKey(redisKey);
		String value = this.redisOperationService.getString(key);
		return code.equalsIgnoreCase(value);
	}
	
	public CaptchaSignedResult encode(String code){
		long validTime = System.currentTimeMillis() + (expireInSeconds * 1000);
		String redisKey = LangUtils.randomUUID();
		redisOperationService.setString(getCaptchaKey(redisKey), code, expireInSeconds);
		return new CaptchaSignedResult(redisKey, validTime);
	}
	
	private String getCaptchaKey(String key) {
		return "captcha:" + key;
	}
	
}

