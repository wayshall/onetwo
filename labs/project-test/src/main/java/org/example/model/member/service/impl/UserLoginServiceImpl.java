package org.example.model.member.service.impl;

import javax.annotation.Resource;

import org.example.model.member.entity.UserEntity;
import org.example.model.member.vo.LoginUserInfo;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.sso.NotSSOAdapterService;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLoginServiceImpl extends NotSSOAdapterService implements Ordered {

	@Resource
	private UserServiceImpl userServiceImpl;
	
	public UserLoginServiceImpl(){
	}
	@Override
	@Transactional
	public LoginUserInfo login(LoginParams loginParams) {
		UserEntity user = userServiceImpl.findUnique("userName", loginParams.getUserName());
		if(user==null || !MDFactory.checkEncrypt(loginParams.getUserPassword(), user.getPassword())){
			throw new LoginException("用户和密码不匹配！");
		}
//		if(user.getStatus()!=UserStatus.NORMAL.getValue()){
//			throw new LoginException("用户状态不正常，拒绝登录！");
//		}
		LoginUserInfo loginUser = new LoginUserInfo();
		loginUser.setUserId(user.getId());
		loginUser.setUserName(user.getUserName());
//		loginUser.setUserRoles(user.getRoles());
		return loginUser;
	}

	@Override
	public int getOrder() {
		return 0;
	}


}
