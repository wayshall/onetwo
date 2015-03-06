package org.onetwo.common.web.login;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.common.web.sso.SimpleNotSSOServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/***
 * 非sso登录抽象类
 * @author wayshall
 *
 * @param <UserData>
 * @param <User>
 */
@Transactional
abstract public class AbstractUserLoginServiceImpl<UserData> extends SimpleNotSSOServiceImpl {
	

	abstract protected ValidatableUser<UserData> findUniqueUser(LoginParams loginParams);
	abstract protected UserDetail createLoginUser(String token, ValidatableUser<UserData> user);
	
	@Override
	public UserDetail login(LoginParams loginParams) {
		ValidatableUser<UserData> user = findUniqueUser(loginParams);//baseEntityManager.findUnique(UserEntity.class, "userName", loginParams.getUserName(), "status:!=", UserStatus.DELETE);
		if(user==null || !MDFactory.checkEncrypt(loginParams.getUserPassword(), user.getPassword())){
			throw new LoginException("用户和密码不匹配！");
		}
		if(user.isInvalidUserStatus()){
			throw new LoginException("用户状态不正常，拒绝登录！");
		}
		String token = generateToken(user);
		return createLoginUser(token, user);
	}
	

	protected String generateToken(ValidatableUser<UserData> user){
		String str = user.getUserName() + System.currentTimeMillis() + LangUtils.getRadomString(6);
		String token = MDFactory.createSHA().encryptWithSalt(str);
		return token;
	}

	@Override
	public void logout(UserDetail userDetail, boolean normal) {
		//do nothing
	}
	
	
	@Override
	public int getOrder() {
		return 0;
	}
}
