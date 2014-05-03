package org.onetwo.common.spring.web.authentic;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.springframework.ui.ModelMap;

public class SecurityWebExceptionResolver extends WebExceptionResolver {

	protected String getAuthenticView(HttpServletRequest request, ModelMap model){
		model.addAttribute(PRE_URL, getPreurl(request));
		AuthenticationContext context = AuthenticUtils.getContextFromRequest(request);
		String view = context!=null?context.getConfig().getRedirect():"";
		return view;
	}
}
