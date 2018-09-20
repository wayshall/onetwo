package org.onetwo.common.web.captcha;

import lombok.Data;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.md.MessageDigestHasher;
import org.slf4j.Logger;
import org.springframework.util.Assert;

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
	public static class HashCaptchaChecker implements CaptchaChecker {
		final private String salt;
		final private int expireInSeconds;
		final private MessageDigestHasher hasher = Hashs.SHA;

		public HashCaptchaChecker(String salt, int validInSeconds) {
			super();
			this.salt = salt;
			Assert.isTrue(validInSeconds>0, "validInSeconds["+validInSeconds+"] must greater than 0");
			this.expireInSeconds = validInSeconds;
		}
		
		public boolean check(String code, String hashStr){
			long validTime = getValidTime();
			String source = code+salt+validTime;
			boolean res = hasher.checkHash(source, hashStr);
			return res;
		}
		
		public CaptchaSignedResult encode(String code){
			long validTime = getValidTime();
			Logger logger = JFishLoggerFactory.getCommonLogger();
			if(logger.isInfoEnabled()){
				logger.info("signing validTime: {}", validTime);
			}
			String source = code+salt+validTime;
			String signed = hasher.hash(source);
			return new CaptchaSignedResult(signed, validTime);
		}
		
		protected long getValidTime(){
			//四舍五入，避免接近边界值时，瞬时失效
			long validTime = Math.round(System.currentTimeMillis() / 1000.0 / expireInSeconds);
			return validTime;
		}
	}
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
