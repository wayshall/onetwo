package org.onetwo.plugins.admin.model.service;

import java.util.Collection;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.plugins.admin.model.vo.ExtMenuModel;

public interface MenuItemRegistry {
	
	public Collection<ExtMenuModel> findAllMenus();
	public Collection<ExtMenuModel> findUserMenus(UserDetail loginUser);

}
