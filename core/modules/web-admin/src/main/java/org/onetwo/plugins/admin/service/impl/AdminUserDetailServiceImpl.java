package org.onetwo.plugins.admin.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.plugins.admin.dao.AdminPermissionDao;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private BaseEntityManager baseEntityManager;
	@Autowired
	private AdminPermissionDao adminPermissionDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<AdminUser> users = baseEntityManager.findList(AdminUser.class, "userName", username);
		AdminUser user = users.stream().findFirst().orElseThrow( ()-> new UsernameNotFoundException(username));
		
		List<GrantedAuthority> authes = Collections.emptyList();
		if(user.getId().longValue()==LoginUserDetails.ROOT_USER_ID){
			List<AdminPermission> perms = adminPermissionDao.findAppPermissions(null);
			authes = perms.stream().map(perm->new SimpleGrantedAuthority(perm.getCode()))
						.collect(Collectors.toList());
		}else{
			List<AdminPermission> perms = this.adminPermissionDao.findAppPermissionsByUserId(null, user.getId());
			authes = perms.stream().map(perm->new SimpleGrantedAuthority(perm.getCode()))
						.collect(Collectors.toList());
		}
		UserDetails userDetail = new LoginUserDetails(user.getId(), user.getUserName(), user.getPassword(), authes);
		return userDetail;
	}
	
	

}
