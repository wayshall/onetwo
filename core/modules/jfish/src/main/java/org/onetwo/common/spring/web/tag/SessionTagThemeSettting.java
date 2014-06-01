package org.onetwo.common.spring.web.tag;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.DefaultTagThemeSetting;
import org.onetwo.common.web.view.TagThemeSetting;

public class SessionTagThemeSettting extends DefaultTagThemeSetting {
	public static final String CONFIG_KEY = "session";
	private static final String THEME_KEY = TagThemeSetting.class.getSimpleName()+"_tag_theme_key";
	private static final String LAYOUT_KEY = TagThemeSetting.class.getSimpleName()+"_layout_key";

	@Override
	public String getTheme() {
		String theme = JFishWebUtils.session(THEME_KEY);
		return StringUtils.isBlank(theme)?super.getTheme():theme;
	}

	@Override
	public String getLayout() {
		String layout = JFishWebUtils.session(LAYOUT_KEY);
		return StringUtils.isBlank(layout)?super.getLayout():layout;
	}

	public void config(String theme, String layout) {
		this.setTheme(theme);
		this.setLayout(layout);
	}

	public void setTheme(String theme) {
		JFishWebUtils.session(THEME_KEY, theme);
	}

	public void setLayout(String layout) {
		JFishWebUtils.session(LAYOUT_KEY, layout);
	}

}
