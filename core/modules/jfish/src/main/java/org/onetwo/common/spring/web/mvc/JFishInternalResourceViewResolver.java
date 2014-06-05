package org.onetwo.common.spring.web.mvc;

import org.onetwo.common.web.view.ThemeSetting;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class JFishInternalResourceViewResolver extends InternalResourceViewResolver {

	private ThemeSetting themeSetting;
	
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		viewName = themeSetting.getViewPage(viewName);
		return super.buildView(viewName);
	}

	public ThemeSetting getThemeSetting() {
		return themeSetting;
	}

	public void setThemeSetting(ThemeSetting themeSetting) {
		this.themeSetting = themeSetting;
	}

}
