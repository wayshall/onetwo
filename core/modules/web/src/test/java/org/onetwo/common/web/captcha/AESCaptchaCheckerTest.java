package org.onetwo.common.web.captcha;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.captcha.CaptchaChecker.CaptchaSignedResult;

/**
 * @author weishao zeng
 * <br/>
 */
public class AESCaptchaCheckerTest {

	String key = "FoyRSp4wy0e9OpIxFHjQvQIO38Nv4VPXqbJrtscNeGFasdYHYzbJee4XVasdfasfZe2knLabXre3";
//	String key = "1234567812345678";
	private AESCaptchaChecker checker = new AESCaptchaChecker(key, 3);
	
	@Test
	public void test() {
		String code = "aabb";
		CaptchaSignedResult result = checker.encode(code);
		boolean res = checker.check("aaaa", result.getSigned());
		assertThat(res).isFalse();
		res = checker.check("bbb", "test");
		assertThat(res).isFalse();
		res = checker.check(code, result.getSigned());
		assertThat(res).isTrue();
		
		LangUtils.await(3);
		res = checker.check(code, result.getSigned());
		assertThat(res).isFalse();
		
	}
}

