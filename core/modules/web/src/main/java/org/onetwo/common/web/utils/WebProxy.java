package org.onetwo.common.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wayshall
 * <br/>
 */
public interface WebProxy {

	void doProxy(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse);

}