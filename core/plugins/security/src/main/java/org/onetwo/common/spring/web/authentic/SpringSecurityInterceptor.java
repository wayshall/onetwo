package org.onetwo.common.spring.web.authentic;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.SecurityInterceptor;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserActivityCheckable;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.onetwo.common.web.s2.security.AuthenticationInvocation;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.AuthenticConfigService;
import org.onetwo.common.web.sso.SimpleNotSSOServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;

public class SpringSecurityInterceptor extends SecurityInterceptor implements InitializingBean, ApplicationContextAware, Ordered {

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private AuthenticConfigService authenticConfigService;
	
	@Resource
	private SessionStorer sessionStorer;
	
//	@Resource
//	private SsoConfig ssoConfig;
	
	private RequestPreventor csrfPreventor = PreventorFactory.getCsrfPreventor();

	public SpringSecurityInterceptor(){
	}
	
	
	
	protected SessionStorer getSessionStorer() {
		return sessionStorer;
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		this.authenticConfigService = SpringUtils.getHighestOrder(applicationContext, AuthenticConfigService.class);
		if(this.authenticConfigService==null){
//			this.authenticConfigService = new SpringAuthenticConfigService(applicationContext);
			this.authenticConfigService = SpringUtils.registerBean(applicationContext, AuthenticConfigService.NAME, SpringAuthenticConfigService.class);

			/**if(SpringUtils.getBeans(applicationContext, SSOService.class).isEmpty()){
				SpringUtils.registerBean(applicationContext, SimpleNotSSOServiceImpl.class);
			}
			**/
			
			if(applicationContext.getBeansOfType(AuthenticationInvocation.class).isEmpty()){
				SpringUtils.registerBean(applicationContext, AuthenticationInvocation.NAME, SpringAuthenticationInvocation.class);
			}
		}
		Assert.notNull(sessionStorer);
//		Assert.notNull(this.authenticConfigService, "没有配置验证服务！");
	}

	@Override
	public void doValidate(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SecurityTarget target = createSecurityTarget(request, response, handler);
		if(target==null){
//			logger.info(request.getRequestURL() + " can not create target, ignore it.");
			return ;
		}
		AuthenticUtils.setIntoRequest(request, target);
		
		// 验证配置读取服务
		AuthenticConfigService service = getAuthenticConfigService();
		
		AuthenticConfig config = service.getConfig(target);
		if(BaseSiteConfig.getInstance().isDev())
			logger.info(request.getRequestURL() + " AuthenticConfig: " + config.toString());

		//验证逻辑
		AuthenticationInvocation authentication = service.getAuthenticationInvocation(config);
		UserDetail authoritable = target.getAuthoritable();
		
		AuthenticationContext context = AuthenticationContext.create(config, target);
		this.setIntoRequest(request, context);
		
		HandlerMethod hm = getHandlerMethod(handler);
		if(hm!=null && MethodAuthenticator.class.isInstance(hm.getBean())){
			MethodAuthenticator methodAuth = (MethodAuthenticator)hm.getBean();
			methodAuth.authenticate(authentication, context);
		}else{
			authentication.authenticate(context);
		}

		if(UserActivityCheckable.class.isInstance(authoritable)){
			UserActivityCheckable checkable = (UserActivityCheckable)authoritable;
			checkable.setLastActivityTime(new Date());
		}
		
		if(BaseSiteConfig.getInstance().isSafeRequest())
			csrfPreventor.validateToken(hm.getMethod(), request, response);
		
	}
	
	protected void setIntoRequest(HttpServletRequest request, AuthenticationContext context){
		AuthenticUtils.setIntoRequest(request, context);
	}
	
	protected SecurityTarget createSecurityTarget(HttpServletRequest request, HttpServletResponse response, Object handler){
		if(handler instanceof HandlerMethod){
			return new SpringSecurityTarget(sessionStorer, request, response, (HandlerMethod)handler);
		}
		return null;
		/*SecurityTarget target = null;
		if(handler instanceof HandlerMethod){
			if(ssoConfig.isServerSide()){
				target = new SpringSsoSecurityTarget(sessionStorer, request, response, (HandlerMethod)handler);
			}else{
				target = new SpringSecurityTarget(sessionStorer, request, response, (HandlerMethod)handler);
			}
			AuthenticUtils.setIntoRequest(request, target);
		}
		return target;*/
	}


	/***
	 * 验证配置管理类
	 * @return
	 */
	public AuthenticConfigService getAuthenticConfigService() {
		return this.authenticConfigService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected HandlerMethod getHandlerMethod(Object handler){
		if(handler instanceof HandlerMethod){
			return (HandlerMethod)handler;
		}
		return null;
	}


}
