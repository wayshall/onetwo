package org.onetwo.ext.permission.service;

import java.util.List;

import org.onetwo.common.web.userdetails.UserDetail;

public interface MenuItemRepository<T> {

//	@PreAuthorize("hasRole('ADMIN')")
	public List<T> findAllMenus();
	public List<T> findUserMenus(UserDetail loginUser);

}
