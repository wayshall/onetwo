package org.onetwo.common.spring.web.tag;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.DefaultTagThemeSetting;
import org.onetwo.common.web.view.ThemeSetting;

public class SessionTagThemeSettting extends DefaultTagThemeSetting {
	public static final String CONFIG_KEY = "session";
	private static final String THEME_TAG_KEY = ThemeSetting.class.getSimpleName()+"_tag_theme_key";
	private static final String LAYOUT_KEY = ThemeSetting.class.getSimpleName()+"_layout_key";
	private static final String THEME_VIEW_KEY = ThemeSetting.class.getSimpleName()+"_view_key";

	@Override
	public String getViewPage(String path) {
		return getThemeView() + path;
	}

	@Override
	public String getThemeView() {
		String view = JFishWebUtils.session(THEME_VIEW_KEY);
		return StringUtils.isBlank(view)?super.getThemeView():view;
	}
	
	@Override
	public String getThemeTag() {
		String theme = JFishWebUtils.session(THEME_TAG_KEY);
		return StringUtils.isBlank(theme)?super.getThemeTag():theme;
	}

	@Override
	public String getThemeLayoutDefaultPage() {
		String layout = JFishWebUtils.session(LAYOUT_KEY);
		return StringUtils.isBlank(layout)?super.getThemeLayoutDefaultPage():layout;
	}

	public void config(String theme, String layout, String view) {
		this.setThemeTag(theme);
		this.setThemeLayoutDefaultPage(layout);
		this.setThemeView(view);
	}

	public void setThemeTag(String theme) {
		JFishWebUtils.session(THEME_TAG_KEY, theme);
	}

	public void setThemeLayoutDefaultPage(String layout) {
		JFishWebUtils.session(LAYOUT_KEY, layout);
	}

	public void setThemeView(String view) {
		JFishWebUtils.session(THEME_VIEW_KEY, view);
	}

}
