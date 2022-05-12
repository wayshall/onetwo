package org.onetwo.common.utils;

import java.util.List;
import java.util.regex.Pattern;

import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.exception.ServiceException;

import com.google.common.collect.ImmutableList;

/**
 * @author weishao zeng
 * <br/>
 */
public class PasswordChecker {
	/***
	 * 字母或汉字开头
	 * 至少一个汉字、数字、字母、下划线: "[a-zA-Z0-9_\u4e00-\u9fa5]+"
	 */
	private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9_\u4e00-\u9fa5]+");
	
	/****
	 * 是否有效的用户名
	 * @author weishao zeng
	 * @param str
	 * @return
	 */
	public static boolean isValidUserName(String str){
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return USER_NAME_PATTERN.matcher(str).matches();
	}
	public static void checkValidUserName(String str){
		if (!isValidUserName(str)) {
			throw new ServiceException("无效用户名");
		}
	}
	
	static private final List<Character> SPECIAL_CHARS = ImmutableList.of('~', '!', '@', '#', '$', '%', 
			'^', '&', '*', '(', ')', '-', '_', 
			'+', '=', '[', ']', ';', ':', '\'',
			'"', '?', ',', '.', '/', '\\', '{', '}');
	
	/***
	 * 最小长度
	 */
	private int minLength = 8;
	
	private int maxLength = 128;
	/***
	 * 必须包含数字的个数
	 * 若配置了数量少于1，则不检查此规则
	 */
	private int digitCount = 1;
	/***
	 * 必须包含大写字母的个数
	 * 若配置了数量少于1，则不检查此规则
	 */
	private int upperCaseCount = 1;
	/***
	 * 必须包含特殊字符的个数
	 * 若配置了数量少于1，则不检查此规则
	 */
	private int specialCharCount = 1;
	private List<Character> specialChars = SPECIAL_CHARS;
	
	public void doCheck(String password) {
		checkMinLength(password);
		checkMaxLength(password);
		checkDigitalCount(password);
		checkUpperCaseCount(password);
		checkSpecialCharCount(password);
	}
	
	private void checkMinLength(String password) {
		if (minLength<1) {
			throw ServiceException.formatMessage(PasswordErrors.ERR_CONFIG_MIN_LENGTH);
		}
        if (StringUtils.isBlank(password) || password.length() < minLength) {
            throw ServiceException.formatMessage(PasswordErrors.ERR_MIN_LENGTH, minLength);
        }
	}
	
	private void checkMaxLength(String password) {
		if (maxLength<1) {
			throw ServiceException.formatMessage(PasswordErrors.ERR_CONFIG_MAX_LENGTH);
		}
        if (password.length() > maxLength) {
            throw ServiceException.formatMessage(PasswordErrors.ERR_MAX_LENGTH, maxLength);
        }
	}
	
	private void checkDigitalCount(String password) {
		// 若配置了数量少于1，则不检查此规则
		if (digitCount < 1) {
			return ;
		}
		int pwdDigitCount = 0;
        for (char ch : password.toCharArray()) {
        	if (Character.isDigit(ch)) {
        		pwdDigitCount++;
        	}
        }
        if (pwdDigitCount<digitCount) {
            throw ServiceException.formatMessage(PasswordErrors.ERR_DIGIT_COUNT, digitCount);
        }
	}
	
	private void checkUpperCaseCount(String password) {
		// 若配置了数量少于1，则不检查此规则
		if (upperCaseCount < 1) {
			return ;
		}
		int pwdUpperCount = 0;
        for (char ch : password.toCharArray()) {
        	if (Character.isUpperCase(ch)) {
        		pwdUpperCount++;
        	}
        }
        if (pwdUpperCount < upperCaseCount) {
            throw ServiceException.formatMessage(PasswordErrors.ERR_UPPER_CASE_COUNT, upperCaseCount);
        }
	}
	
	private void checkSpecialCharCount(String password) {
		// 若配置了数量少于1，则不检查此规则
		if (specialCharCount < 1) {
			return ;
		}
		if (LangUtils.isEmpty(specialChars)) {
			return ;
		}
		int pwdSpecialCount = 0;
        for (Character ch : password.toCharArray()) {
        	if (specialChars.contains(ch)) {
        		pwdSpecialCount++;
        	}
        }
        if (pwdSpecialCount < specialCharCount) {
            throw ServiceException.formatMessage(PasswordErrors.ERR_SPECIAL_CHAR_COUNT, specialCharCount, StringUtils.join(specialChars, ""));
        }
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setDigitCount(int digitCount) {
		this.digitCount = digitCount;
	}

	public void setUpperCaseCount(int upperCaseCount) {
		this.upperCaseCount = upperCaseCount;
	}

	public void setSpecialCharCount(int specialCharCount) {
		this.specialCharCount = specialCharCount;
	}

	public void setSpecialChars(List<Character> specialChars) {
		this.specialChars = specialChars;
	}



	public static enum PasswordErrors implements ErrorType {
		ERR_CONFIG_MIN_LENGTH("密码最小长度不能配置少于1"),
		ERR_CONFIG_MAX_LENGTH("密码最大长度不能配置少于1"),
		
		ERR_MIN_LENGTH("密码长度不少于 %d 位!"),
		ERR_MAX_LENGTH("密码长度不超过 %d 位!"),
		ERR_DIGIT_COUNT("密码必须包含至少 %d 位数字!"),
		ERR_UPPER_CASE_COUNT("密码必须包含至少 %d 位大写字母!"),
		ERR_SPECIAL_CHAR_COUNT("密码必须包含至少 %d 位这些特殊字符: %s")
		;
		
		private final String message;
		
		private PasswordErrors(String message) {
			this.message = message;
		}

		@Override
		public String getErrorCode() {
			return name();
		}

		@Override
		public String getErrorMessage() {
			return message;
		}
	}

	public static class PasswordCheckerBuilder {
		/***
		 * 最小长度
		 */
		private int minLength = 8;
		
		private int maxLength = 128;
		/***
		 * 必须包含大数字的个数
		 * 若配置了数量少于1，则不检查此规则
		 */
		private int digitCount = 1;
		/***
		 * 必须包含大写字母的个数
		 * 若配置了数量少于1，则不检查此规则
		 */
		private int upperCaseCount = 1;
		/***
		 * 必须包含特殊字符的个数
		 * 若配置了数量少于1，则不检查此规则
		 */
		private int specialCharCount = 1;
		private List<Character> specialChars = SPECIAL_CHARS;
		
		public PasswordChecker build() {
			PasswordChecker checker = new PasswordChecker();
			checker.minLength = minLength;
			checker.maxLength = maxLength;
			checker.digitCount = digitCount;
			checker.upperCaseCount = upperCaseCount;
			checker.specialCharCount = specialCharCount;
			if (LangUtils.isNotEmpty(specialChars)) {
				checker.specialChars = specialChars;
			}
			return checker;
		}
		public PasswordCheckerBuilder minLength(int minLength) {
			this.minLength = minLength;
			return this;
		}
		public PasswordCheckerBuilder maxLength(int maxLength) {
			this.maxLength = maxLength;
			return this;
		}
		public PasswordCheckerBuilder digitCount(int digitCount) {
			this.digitCount = digitCount;
			return this;
		}
		public PasswordCheckerBuilder upperCaseCount(int upperCaseCount) {
			this.upperCaseCount = upperCaseCount;
			return this;
		}
		public PasswordCheckerBuilder specialCharCount(int specialCharCount) {
			this.specialCharCount = specialCharCount;
			return this;
		}
		public PasswordCheckerBuilder specialChars(List<Character> specialChars) {
			this.specialChars = specialChars;
			return this;
		}
		
	}
}
