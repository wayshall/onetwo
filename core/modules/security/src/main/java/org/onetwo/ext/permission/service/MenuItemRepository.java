package org.onetwo.ext.permission.service;

import java.util.List;
import java.util.Map;

import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.api.IPermission;

public interface MenuItemRepository<T> {

//	@PreAuthorize("hasRole('ADMIN')")
	public List<T> findAllMenus();
	public List<T> findUserMenus(UserDetail loginUser);
	public <E> List<E> findUserMenus(UserDetail loginUser, TreeMenuBuilder<E> builder);
	
	public <E> List<E> findUserPermissions(UserDetail loginUser, TreeMenuBuilder<E> builder);


	public interface TreeMenuBuilder<T> {
		List<T> build(List<? extends IPermission> permissions, Map<String, ? extends IPermission> allPermissions);
	}
}
