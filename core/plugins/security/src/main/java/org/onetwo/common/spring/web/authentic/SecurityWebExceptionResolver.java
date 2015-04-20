package org.onetwo.common.spring.web.authentic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.onetwo.plugins.security.common.SecurityConfig;
import org.springframework.ui.ModelMap;

public class SecurityWebExceptionResolver extends WebExceptionResolver {
	
//	public static final String DEFAULT_LOGIN_VIEW = "redirect:/plugin-security/client/login";
//	public static final String DEFAULT_LOGIN_VIEW = SecurityPluginUtils.COMMON_LOGIN_URL;
	@Resource
	private SecurityConfig securityConfig;

	protected void initResolver(){
//		defaultRedirect = securityConfig.getAuthenticRedirectUrl();
	}
	protected String getLoginView(HttpServletRequest request, ModelMap model){
		AuthenticationContext context = AuthenticUtils.getContextFromRequest(request);
		String view = (context!=null && StringUtils.isNotBlank(context.getConfig().getRedirect()))?context.getConfig().getRedirect():securityConfig.getAuthenticRedirectUrl();
		/*if(StringUtils.isBlank(view)){
			view = securityConfig.getAuthenticRedirectUrl();
		}*/
		view = JFishWebUtils.redirect(view);
//		String view = ssoConfig.getLoginUrl();
//		view = TagUtils.appendParam(view, "returnUrl", LangUtils.encodeUrl(ssoConfig.getReturnUrl()));
//		view = TagUtils.appendParam(view, SecurityPluginUtils.LOGIN_PARAM_CLIENT_CODE, BaseSiteConfig.getInstance().getAppCode());
		return view;
	}
}
