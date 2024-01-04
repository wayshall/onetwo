package org.onetwo.common.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author wayshall
 * <br/>
 */
public interface WebProxy {

	void doProxy(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse);

}