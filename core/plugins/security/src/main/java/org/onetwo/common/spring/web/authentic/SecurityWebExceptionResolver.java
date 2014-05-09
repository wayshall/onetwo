package org.onetwo.common.spring.web.authentic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.plugins.security.common.SsoConfig;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.ui.ModelMap;

public class SecurityWebExceptionResolver extends WebExceptionResolver {
	
	@Resource
	private SsoConfig ssoConfig;

	protected String getAuthenticView(HttpServletRequest request, ModelMap model){
//		model.addAttribute(PRE_URL, getPreurl(request));
		/*String returnUrl = getPreurl(request);
		if(StringUtils.isBlank(returnUrl)){
			returnUrl = ssoConfig.getReturnUrl();
		}*/
		AuthenticationContext context = AuthenticUtils.getContextFromRequest(request);
		String view = context!=null?context.getConfig().getRedirect():"";
		if(StringUtils.isBlank(view)){
			view = ssoConfig.getLoginUrl();
		}
//		view = TagUtils.appendParam(view, "returnUrl", LangUtils.encodeUrl(ssoConfig.getReturnUrl()));
		view = TagUtils.appendParam(view, SecurityPluginUtils.LOGIN_PARAM_CLIENT_CODE, BaseSiteConfig.getInstance().getAppCode());
		return "redirect:"+view;
	}
}
