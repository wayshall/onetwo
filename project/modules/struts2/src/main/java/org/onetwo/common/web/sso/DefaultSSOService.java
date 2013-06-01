package org.onetwo.common.web.sso;

import java.util.Map;

import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.annotation.BeanOrder;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.utils.SessionUtils;
import org.springframework.stereotype.Service;

@SuppressWarnings("rawtypes")
@Service
@BeanOrder(Integer.MAX_VALUE)
public class DefaultSSOService implements SSOService{

	@Override
	public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime) {
		return target.getAuthoritable()==null;
	}

	@Override
	public UserDetail checkLogin(SecurityTarget target) {
		return SessionUtils.getUserDetail();
	}

	@Override
	public UserDetail login(String username, String password, Map params) {
		throw new UnsupportedOperationException("no login");
	}

	@Override
	public void logout(UserDetail userDetail, boolean normal) {
		SessionUtils.removeUserDetail();
	}

}
