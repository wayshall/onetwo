package org.onetwo.common.spring.web.authentic;

import java.util.Map;

import org.onetwo.common.sso.UserLoginService;
import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.utils.DefaultUserDetail;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.sso.AbstractSSOServiceImpl;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
public class DefaultSpringSSOService extends AbstractSSOServiceImpl implements UserLoginService, Ordered {
	
	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		throw LangUtils.asBaseException("un supported operation!");
	}

	@Override
	protected UserDetail getCurrentLoginUserByCookieToken(String token) {
		return null;
	}

	/*@Override
	protected UserDetail createUserDetail(SecurityTarget target) {
		UserDetail user = new DefaultUserDetail();
		user.setToken(target.getCookieToken());
		return user;
	}*/

	@SuppressWarnings("rawtypes")
	@Override
	public UserDetail login(String username, String password, Map params) {
		throw LangUtils.asBaseException("un supported operation!");
	}

	@Override
	public void logout(UserDetail userDetail, boolean normal) {
		throw LangUtils.asBaseException("un supported operation!");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
