package org.onetwo.ext.permission.service;

import java.util.List;
import java.util.Map;

import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.entity.DefaultIPermission;

public interface MenuItemRepository<T> {

//	@PreAuthorize("hasRole('ADMIN')")
	public List<T> findAllMenus();
	public List<T> findUserMenus(UserDetail loginUser);
	
	public List<T> findUserPermissions(UserDetail loginUser, TreeMenuBuilder<T> builder);


	public interface TreeMenuBuilder<T> {
		@SuppressWarnings("rawtypes")
		List<T> build(List<? extends DefaultIPermission> permissions, Map<String, ? extends DefaultIPermission> allPermissions);
	}
}
