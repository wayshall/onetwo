package org.onetwo.common.web.captcha;

import java.util.Arrays;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.convert.Types;
import org.onetwo.common.encrypt.EncryptCoder;
import org.onetwo.common.encrypt.EncryptCoderFactory;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.md.CodeType;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */
public class AESCaptchaChecker implements CaptchaChecker {
	final private int expireInSeconds;
	final private EncryptCoder<SecretKey> aesCoder;
	final private Logger logger = JFishLoggerFactory.getCommonLogger();
	final private CodeType codeType = CodeType.HEX;

	public AESCaptchaChecker(final String salt, int validInSeconds) {
		super();
		Assert.isTrue(validInSeconds>0, "validInSeconds["+validInSeconds+"] must greater than 0");
		String realKey = salt;
		if (salt.length()!=16 || salt.length()!=24 || salt.length()!=32) {
//			throw new IllegalArgumentException("error key size: " + salt.length());
			if (salt.length()>32) {
				realKey = salt.substring(0, 32);
			} else if (salt.length()>24) {
				realKey = salt.substring(0, 24);
			} else {
				realKey = salt.substring(0, 16);
			}
			logger.info("aes key size[{}] error, auto cust as : {}", salt.length(), realKey);
		}
		this.expireInSeconds = validInSeconds;
		this.aesCoder = EncryptCoderFactory.aesCbcCoder(realKey);
	}
	
	public boolean check(String code, String hashStr){
		byte[] dencryptData = null;
		try {
			dencryptData = aesCoder.dencrypt(aesCoder.getKey(), codeType.decode(hashStr, FileUtils.UTF8));
		} catch (Exception e) {
			logger.error("dencrypt error, value: " + hashStr, e);
			return false;
		}
		String[] values = splitContent(dencryptData);
		if (values.length!=2) {
			logger.warn("captcha data error: {}", StringUtils.join(values, ", "));
			return false;
		}
		long validTime = Types.asLong(values[1]);
		if (System.currentTimeMillis() > validTime) {
			logger.warn("captcha data has bean expired {}", validTime);
			return false;
		}
		boolean res = code.equalsIgnoreCase(values[0]);
		return res;
	}
	
	public CaptchaSignedResult encode(String code){
		long validTime = System.currentTimeMillis() + (expireInSeconds * 1000);
		String content = contentAsString(code, validTime);
		byte[] encrpted = aesCoder.encrypt(aesCoder.getKey(), LangUtils.getBytes(content));
		String signed = codeType.encode(encrpted, FileUtils.UTF8);
		return new CaptchaSignedResult(signed, validTime);
	}
	
	private String contentAsString(String code, long validTime) {
		String content = StringUtils.join(Arrays.asList(code, validTime), ":");
		return content;
	}
	
	private String[] splitContent(byte[] dencryptData) {
		String content = LangUtils.newString(dencryptData);
		return StringUtils.splitByWholeSeparator(content, ":");
	}
	
}

