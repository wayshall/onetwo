package org.onetwo.common.web.sso;

import javax.annotation.Resource;

import org.onetwo.common.sso.CurrentLoginUserParams;
import org.onetwo.common.utils.UserDetail;
import org.springframework.transaction.annotation.Transactional;

abstract public class AbstractSSOUserService<T extends UserDetail> implements SSOUserService {

	@Resource
	private SSOUserService ssoUserServiceProxy;
	

	/*********
	 * 
	 */
	@Transactional
	@Override
	public UserDetail getCurrentLoginUser(CurrentLoginUserParams params) {
		UserDetail userDetail = ssoUserServiceProxy.getCurrentLoginUser(params);
		if(userDetail==null)
			return null;
		return createLoginUser(userDetail);
	}
	
	/******
	 * 
	 * @param userDetail 登录服务器返回的登录用户信息
	 * @return
	 */
	abstract protected T createLoginUser(UserDetail userDetail);
}
