package org.onetwo.plugins.admin.model.service;

import java.util.Collection;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.plugins.admin.model.vo.ExtMenuModel;

public interface MenuItemRegistry {
	
	Collection<ExtMenuModel> findAllMenus();
	Collection<ExtMenuModel> findUserMenus(UserDetail loginUser);

}
