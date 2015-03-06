package org.onetwo.common.spring.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmptySecurityInterceptor extends SecurityInterceptor {

	@Override
	protected void doValidate(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
	}


}
