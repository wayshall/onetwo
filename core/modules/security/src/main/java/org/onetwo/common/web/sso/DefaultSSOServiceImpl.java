package org.onetwo.common.web.sso;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.UserDetail;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

public class DefaultSSOServiceImpl extends AbstractSSOServiceImpl implements InitializingBean, Ordered {

	private SSOUserService ssoUserService;
	private UserActivityTimeHandler userActivityTimeHandler;
	
	@Resource
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(ssoUserService==null)
			ssoUserService = SpringUtils.getBean(applicationContext, SSOUserService.class);
		Assert.notNull(ssoUserService);
	}
	
	

	@Override
	protected UserDetail getCurrentLoginUserByCookieToken(String token) {
		return ssoUserService.getCurrentLoginUserByToken(token);
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
