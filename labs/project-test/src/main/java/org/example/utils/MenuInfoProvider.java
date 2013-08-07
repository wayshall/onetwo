package org.example.utils;

import org.onetwo.plugins.permission.MenuInfoable;
import org.springframework.stereotype.Component;

@Component
public class MenuInfoProvider implements MenuInfoable {

	@Override
	public Class<?> getRootMenuClass() {
		return MenuInfo.class;
	}

}
