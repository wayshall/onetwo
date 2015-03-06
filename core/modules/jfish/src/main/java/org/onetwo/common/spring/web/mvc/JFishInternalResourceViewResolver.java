package org.onetwo.common.spring.web.mvc;

import java.util.Locale;

import javax.annotation.Resource;

import org.onetwo.common.web.view.ThemeSetting;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class JFishInternalResourceViewResolver extends InternalResourceViewResolver {

	@Resource
	private ThemeSetting themeSetting;

	public ThemeSetting getThemeSetting() {
		return themeSetting;
	}

	public void setThemeSetting(ThemeSetting themeSetting) {
		this.themeSetting = themeSetting;
	}
	
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		String themeViewName = themeSetting.getViewPage(viewName);
		return super.resolveViewName(themeViewName, locale);
	}

}
