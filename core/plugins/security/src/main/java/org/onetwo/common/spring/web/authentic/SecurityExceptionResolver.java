package org.onetwo.common.spring.web.authentic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

/************
 * 异常处理
 * @author wayshall
 *
 */
public class SecurityExceptionResolver extends AbstractHandlerMethodExceptionResolver implements InitializingBean {


	public static final String REDIRECT = "redirect:";
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	public SecurityExceptionResolver(){
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
		if(handlerMethod==null)
			return null;

		
		boolean authentic = false;
		if(ex instanceof AuthenticationException){
			authentic = true;
		}else if(ex instanceof LoginException){
			authentic = true;
		}
		if(authentic){
			logger.error("login error: {}", ex.getMessage());
			AuthenticationContext context = AuthenticUtils.getContextFromRequest(request);
			String viewName = context!=null?context.getConfig().getRedirect():"";
			return createModelAndView(viewName, null, request);
		}else{
			logger.error("error: ", ex);
		}
		
		return null;
	}

	protected ModelAndView createModelAndView(String viewName, ModelMap model, HttpServletRequest request){
		if(isRedirect(viewName)){
			if(BaseSiteConfig.getInstance().hasAppUrlPostfix()){
				viewName = BaseSiteConfig.getInstance().appendAppUrlPostfix(viewName);
			}
			return new ModelAndView(viewName, model);
		}else{
			return new ModelAndView(viewName, model);
		}
	}
	public static boolean isRedirect(String viewName){
		return viewName!=null && viewName.startsWith(REDIRECT);
	}
}
