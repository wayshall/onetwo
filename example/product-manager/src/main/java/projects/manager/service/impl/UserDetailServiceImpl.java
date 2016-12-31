package projects.manager.service.impl;

import java.util.List;

import org.onetwo.plugins.admin.service.impl.AdminUserDetailServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projects.manager.entity.User;
import projects.manager.vo.LoginUserInfo;

@Transactional
@Service
public class UserDetailServiceImpl extends AdminUserDetailServiceImpl<User> {

	@Override
	protected UserDetails buildUserDetail(User user, List<GrantedAuthority> authes) {
		LoginUserInfo userDetail = new LoginUserInfo(user.getId(), user.getUserName(), user.getPassword(), authes);
		userDetail.setUserType(user.getUserType());
		return userDetail;
	}
	
}
