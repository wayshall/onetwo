package org.onetwo.common.web.captcha;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.captcha.SimpleCaptchaGenerator.CaptchaResult;
import org.onetwo.common.web.captcha.SimpleCaptchaGenerator.CaptchaSettings;
import org.onetwo.common.web.captcha.Captchas.CaptchaChecker;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleCaptchaGeneratorTest {
	static final int validInSeconds = 3;
	static private final CaptchaChecker checker = new CaptchaChecker("test", validInSeconds);
	
	@Test
	public void testGenerate(){
		CaptchaSettings settings = new CaptchaSettings();
		settings.setWidth(400);
		settings.setHeight(100);
		SimpleCaptchaGenerator generator = new SimpleCaptchaGenerator();
		CaptchaResult res = generator.writeTo(settings, "D:/test/captcha.png");
		
		String signed = checker.sign(res.getCode());
		boolean checked = checker.check(res.getCode(), signed);
		assertThat(checked).isTrue();
		
		LangUtils.await(validInSeconds);
		checked = checker.check(res.getCode(), signed);
		assertThat(checked).isFalse();
	}

}
