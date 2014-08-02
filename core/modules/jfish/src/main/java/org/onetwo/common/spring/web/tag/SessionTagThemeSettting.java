package org.onetwo.common.spring.web.tag;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.DefaultTagThemeSetting;
import org.onetwo.common.web.view.ThemeSetting;

public class SessionTagThemeSettting extends DefaultTagThemeSetting {
	public static final String CONFIG_KEY = "session";
	/*private static final String THEME_TAG_KEY = ThemeSetting.class.getSimpleName()+"_tag_theme_key";
	private static final String LAYOUT_KEY = ThemeSetting.class.getSimpleName()+"_layout_key";
	private static final String THEME_VIEW_KEY = ThemeSetting.class.getSimpleName()+"_view_key";
	private static final String EXT_THEME_KEY = ThemeSetting.class.getSimpleName()+"_ext_theme_key";*/
	
	private static final String THEME_ATTRIBUTES_KEY = ThemeSetting.class.getSimpleName()+"_THEME_ATTRIBUTES_KEY";
	
	
	protected ThemeAttributes initIfNecessity(){
		ThemeAttributes theme = JFishWebUtils.session(THEME_ATTRIBUTES_KEY);
		if(theme==null){
			theme = new ThemeAttributes();
			JFishWebUtils.session(THEME_ATTRIBUTES_KEY, theme);
		}
		return theme;
	}

	@Override
	public String getViewPage(String path) {
		return getThemeView() + path;
	}

	@Override
	public String getThemeView() {
		String view = initIfNecessity().getThemeView();
		return StringUtils.isBlank(view)?super.getThemeView():view;
	}
	
	@Override
	public String getThemeTag() {
		String theme = initIfNecessity().getThemeTag();
		return StringUtils.isBlank(theme)?super.getThemeTag():theme;
	}

	@Override
	public String getThemeLayoutDefaultPage() {
		String layout = initIfNecessity().getLayout();
		return StringUtils.isBlank(layout)?super.getThemeLayoutDefaultPage():layout;
	}
	
	@Override
	public String getExtTheme() {
		String extTheme = initIfNecessity().getExtTheme();
		return StringUtils.isBlank(extTheme)?this.getSiteConfig().getExtTheme():extTheme;
	}
	
	@Override
	public boolean isJsui() {
		return initIfNecessity().isJsui();
	}
	
	public void setJsui(boolean viewJsui){
		initIfNecessity().setJsui(viewJsui);
	}

	public void config(String theme, String layout, String view) {
		this.setThemeTag(theme);
		this.setThemeLayoutDefaultPage(layout);
		this.setThemeView(view);
	}

	public void setThemeTag(String theme) {
		initIfNecessity().setThemeTag(theme);
	}

	public void setThemeLayoutDefaultPage(String layout) {
		initIfNecessity().setLayout(layout);
	}

	public void setThemeView(String view) {
		initIfNecessity().setThemeView(view);
	}
	
	public void setExtTheme(String extTheme){
		initIfNecessity().setExtTheme(extTheme);
	}

}
