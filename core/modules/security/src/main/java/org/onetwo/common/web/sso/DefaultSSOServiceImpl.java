package org.onetwo.common.web.sso;

import org.onetwo.common.sso.UserActivityTimeHandler;
import org.springframework.core.Ordered;

public class DefaultSSOServiceImpl extends AbstractSSOServiceImpl implements Ordered {

	private SSOUserService SSOUserService = new EmptySSOUserServiceImpl();
	private UserActivityTimeHandler userActivityTimeHandler;
	
	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		return userActivityTimeHandler;
	}

	@Override
	public SSOUserService getSSOUserService() {
		return SSOUserService;
	}

	public void setSSOUserService(SSOUserService sSOUserService) {
		SSOUserService = sSOUserService;
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
