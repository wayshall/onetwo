package org.onetwo.common.web.captcha;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.captcha.CaptchaChecker.CaptchaSignedResult;
import org.onetwo.common.web.captcha.CaptchaChecker.HashCaptchaChecker;
import org.onetwo.common.web.captcha.SimpleCaptchaGenerator.CaptchaResult;
import org.onetwo.common.web.captcha.SimpleCaptchaGenerator.CaptchaSettings;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleCaptchaGeneratorTest {
	static final int validInSeconds = 3;
	static private final CaptchaChecker checker = new HashCaptchaChecker("test", validInSeconds);
	
	@Test
	public void testGenerate(){
		CaptchaSettings settings = new CaptchaSettings();
		settings.setWidth(400);
		settings.setHeight(100);
		settings.setPuzzleLineCount(50);
//		settings.setCodeColor("255, 0, 0");
//		settings.setFontHeight(0.8);
		SimpleCaptchaGenerator generator = new SimpleCaptchaGenerator();
		CaptchaResult res = generator.writeTo(settings, "/data/tmp/captcha.png");
		
		CaptchaSignedResult signed = checker.encode(res.getCode());
		boolean checked = checker.check(res.getCode(), signed.getSigned());
		assertThat(checked).isTrue();
		
		LangUtils.await(validInSeconds);
		checked = checker.check(res.getCode(), signed.getSigned());
		assertThat(checked).isFalse();
	}

}
