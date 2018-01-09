package org.onetwo.plugins.admin.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.plugins.admin.dao.AdminPermissionDao;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.utils.Enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
@Transactional(readOnly=true)
public class AdminUserDetailServiceImpl<T extends AdminUser> implements UserDetailsService {

    @Autowired
    protected BaseEntityManager baseEntityManager;
	@Autowired
	protected AdminPermissionDao adminPermissionDao;
	
	protected Class<T> userDetailClass;

	public AdminUserDetailServiceImpl() {
		super();
		this.userDetailClass = (Class<T>)ReflectUtils.getSuperClassGenricType(this.getClass());
	}

	public AdminUserDetailServiceImpl(Class<T> userDetailClass) {
		super();
		this.userDetailClass = userDetailClass;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		T user = fetUserByUserName(username);
		if(!UserStatus.NORMAL.name().equals(user.getStatus())){
			throw new LockedException("用户状态异常："+user.getStatusName());
		}
		
		List<GrantedAuthority> authes = fetchUserGrantedAuthorities(user);
		UserDetails userDetail = buildUserDetail(user, authes);
		return userDetail;
	}
	
	protected List<GrantedAuthority> fetchUserGrantedAuthorities(T user){
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
		return authes;
	}
	
	protected T fetUserByUserName(String username){
		List<T> users = baseEntityManager.findList(userDetailClass, "userName", username);
		T user = users.stream().findFirst().orElseThrow( ()-> new UsernameNotFoundException(username));
		return user;
	}
	
	protected UserDetails buildUserDetail(T user, List<GrantedAuthority> authes){
		UserDetails userDetail = new LoginUserDetails(user.getId(), user.getUserName(), user.getPassword(), authes);
		return userDetail;
	}
	
	

}
