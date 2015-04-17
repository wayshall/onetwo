package org.onetwo.plugins.security.sso;

import javax.annotation.Resource;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.CurrentLoginUserParams;
import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.sso.AbstractSSOServiceImpl;
import org.onetwo.common.web.sso.SSOUserService;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.remoting.RemoteConnectFailureException;

public class DefaultSSOServiceImpl extends AbstractSSOServiceImpl implements InitializingBean, Ordered {

	private SSOUserService ssoUserService;
	private UserActivityTimeHandler userActivityTimeHandler;
	
	@Resource
	private ApplicationContext applicationContext;
	
	@Resource
	private SsoConfig ssoConfig;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(ssoUserService==null)
			ssoUserService = SpringUtils.getHighestOrder(applicationContext, SSOUserService.class);
		Assert.notNull(ssoUserService);
	}
	
	

	@Override
	public UserDetail getCurrentLoginUserByCookieToken(String token) {
		try {
			if(token==null)
				return null;
			String sign = SecurityPluginUtils.sign(token, ssoConfig.getSignKey());
			CurrentLoginUserParams params = new CurrentLoginUserParams(token);
			params.setSign(sign);
			params.setClientCode(BaseSiteConfig.getInstance().getAppCode());
			return ssoUserService.getCurrentLoginUser(params);
		} catch(LoginException e){
			throw e;
		} catch (Exception e) {
			String msg = "";
			if(e instanceof ClassNotFoundException){
				msg = "client no mapped user detail class " + (BaseSiteConfig.getInstance().isProduct()?"":e.getMessage());
				throw new LoginException(msg);
			}else if(e instanceof RemoteConnectFailureException){
				msg = "connect to sso server error: " + e.getMessage();
				throw new LoginException(msg);
			}else{
				logger.error("sso client get login user error : " + msg, e);
				msg = e.getMessage();
				throw new LoginException("sso client get login user error : " + msg, e);
			}
		}
	}



	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		return userActivityTimeHandler;
	}


	public void setSsoUserService(SSOUserService sSOUserService) {
		this.ssoUserService = sSOUserService;
	}

	public void setUserActivityTimeHandler(
			UserActivityTimeHandler userActivityTimeHandler) {
		this.userActivityTimeHandler = userActivityTimeHandler;
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
