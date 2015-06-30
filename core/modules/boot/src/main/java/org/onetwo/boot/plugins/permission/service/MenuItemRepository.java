package org.onetwo.boot.plugins.permission.service;

import java.util.Collection;

import org.springframework.security.access.prepost.PreAuthorize;

public interface MenuItemRepository<T> {

	@PreAuthorize("hasRole('ADMIN')")
	public Collection<T> findAllMenus();
//	public Collection<T> findUserMenus(UserDetail loginUser);

}
