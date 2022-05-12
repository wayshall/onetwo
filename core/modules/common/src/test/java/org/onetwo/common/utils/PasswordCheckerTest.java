package org.onetwo.common.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.PasswordChecker.PasswordCheckerBuilder;
import org.onetwo.common.utils.PasswordChecker.PasswordErrors;

public class PasswordCheckerTest {
	
	@Test
	public void testPassword() {
		PasswordChecker checker = new PasswordCheckerBuilder().build();
		
		try {
			checker.doCheck("aaa");
			fail();
		} catch (ServiceException e) {
			assertThat(e.getExceptionType()).isEqualTo(PasswordErrors.ERR_MIN_LENGTH);
		}
		
		try {
			checker.doCheck("sadfasdfasdfasdfasdfasd;ofkas'pdfap'sdjfoajdf;oja;sdfj;asjdfasl;df;lasdfa;sdfa;sdfladfasdfasd;ofkas'pdfap'sdjfoajdf;oja;sdfj;asjdfasl;df;lasdfa;sdfa;sdfladfasdfasd;ofkas'pdfap'sdjfoajdf;oja;sdfj;asjdfasl;df;lasdfa;sdfa;sdfladfasdfasd;ofkas'pdfap'sdjfoajdf;oja;sdfj;asjdfasl;df;lasdfa;sdfa;sdflad");
			fail();
		} catch (ServiceException e) {
			assertThat(e.getExceptionType()).isEqualTo(PasswordErrors.ERR_MAX_LENGTH);
		}
		
		try {
			checker.doCheck("sdsdfasdf");
			fail();
		} catch (ServiceException e) {
			assertThat(e.getExceptionType()).isEqualTo(PasswordErrors.ERR_DIGIT_COUNT);
		}
		
		try {
			checker.doCheck("sdsdfa11sdf");
			fail();
		} catch (ServiceException e) {
			assertThat(e.getExceptionType()).isEqualTo(PasswordErrors.ERR_UPPER_CASE_COUNT);
		}
		
		try {
			checker.doCheck("sdsDfa11sdf");
			fail();
		} catch (ServiceException e) {
			assertThat(e.getExceptionType()).isEqualTo(PasswordErrors.ERR_SPECIAL_CHAR_COUNT);
		}
		
		checker.doCheck("sdsDfa1&1sdf");
	}
	

	@Test
	public void testIsValidUserName() {
		String userName = "1test";
		boolean isValid = PasswordChecker.isValidUserName(userName);
		assertThat(isValid).isFalse();
		
		userName = "^test";
		isValid = PasswordChecker.isValidUserName(userName);
		assertThat(isValid).isFalse();
		
		userName = " test";
		isValid = PasswordChecker.isValidUserName(userName);
		assertThat(isValid).isFalse();
		
		userName = "user@2022";
		isValid = PasswordChecker.isValidUserName(userName);
		assertThat(isValid).isFalse();
		
		userName = "user_2022";
		isValid = PasswordChecker.isValidUserName(userName);
		assertThat(isValid).isTrue();
		
		userName = "测试user";
		isValid = PasswordChecker.isValidUserName(userName);
		assertThat(isValid).isTrue();
	}

}
