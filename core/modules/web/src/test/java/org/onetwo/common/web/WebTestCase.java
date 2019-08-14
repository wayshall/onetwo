package org.onetwo.common.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.web.captcha.AESCaptchaCheckerTest;
import org.onetwo.common.web.captcha.SimpleCaptchaGeneratorTest;

/**
 * @author weishao zeng
 * <br/>
 */
@RunWith(Suite.class)
@SuiteClasses({
	SimpleCaptchaGeneratorTest.class,
	AESCaptchaCheckerTest.class
})
public class WebTestCase {

}

