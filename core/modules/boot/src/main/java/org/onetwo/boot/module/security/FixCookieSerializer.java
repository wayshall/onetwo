package org.onetwo.boot.module.security;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 此实现修复一个很奇怪都bug，某些情况下（详细未知）请求会携带两个sid都cookies，而第一个是过期的，第二个才是有效的
 * @author weishao zeng
 * <br/>
 */
public class FixCookieSerializer extends DefaultCookieSerializer {

	public List<String> readCookieValues(HttpServletRequest request) {
		List<String> sids = super.readCookieValues(request);
		return Arrays.asList(sids.get(sids.size()-1));
	}

}
