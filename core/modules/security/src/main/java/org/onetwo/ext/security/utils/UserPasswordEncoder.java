package org.onetwo.ext.security.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserPasswordEncoder {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	public String encode(String rawPassword) {
		if (StringUtils.isBlank(rawPassword)) {
			throw new ServiceException("密码不能为空！");
		}
		return passwordEncoder.encode(rawPassword);
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

}
