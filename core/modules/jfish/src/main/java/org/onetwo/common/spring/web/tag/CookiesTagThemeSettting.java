package org.onetwo.common.spring.web.tag;

import java.util.Map;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.common.web.view.DefaultTagThemeSetting;

public class CookiesTagThemeSettting extends DefaultTagThemeSetting {
	public static final String CONFIG_KEY = "cookies";
	/*private static final String THEME_TAG_KEY = ThemeSetting.class.getSimpleName()+"_tag_theme_key";
	private static final String LAYOUT_KEY = ThemeSetting.class.getSimpleName()+"_layout_key";
	private static final String THEME_VIEW_KEY = ThemeSetting.class.getSimpleName()+"_view_key";
	private static final String EXT_THEME_KEY = ThemeSetting.class.getSimpleName()+"_ext_theme_key";*/

	private static final String THEME_TAG = "themeTag";
	private static final String LAYOUT = "layout";
	private static final String THEME_VIEW = "themeView";
	private static final String EXT_THEME = "extTheme";
	private static final String JSUI = "jsui";
	
//	private SessionTagThemeSettting session = new SessionTagThemeSettting();
	

	protected ThemeAttributes initIfNecessity(){
		ThemeAttributes theme = JFishWebUtils.session(SessionTagThemeSettting.THEME_ATTRIBUTES_KEY);
		if(theme==null){
			theme = new ThemeAttributes();
			JFishWebUtils.session(SessionTagThemeSettting.THEME_ATTRIBUTES_KEY, theme);
		}
		return theme;
	}

	@Override
	public String getThemeView() {
		String view = initIfNecessity().getThemeView();
		view = StringUtils.isBlank(view)?ResponseUtils.getCookieValue(WebHolder.getRequest(), THEME_VIEW):view;
		return StringUtils.isBlank(view)?super.getThemeView():view;
	}
	
	@Override
	public String getThemeTag() {
		String theme = initIfNecessity().getThemeTag();
		theme = StringUtils.isBlank(theme)?ResponseUtils.getCookieValue(WebHolder.getRequest(), THEME_TAG):theme;
		return StringUtils.isBlank(theme)?super.getThemeTag():theme;
	}

	@Override
	public String getThemeLayoutDefaultPage() {
		String layout = initIfNecessity().getLayout();
		layout = StringUtils.isBlank(layout)?ResponseUtils.getCookieValue(WebHolder.getRequest(), LAYOUT):layout;
		return StringUtils.isBlank(layout)?super.getThemeLayoutDefaultPage():layout;
	}
	
	@Override
	public String getExtTheme() {
		String extTheme = ResponseUtils.getCookieValue(WebHolder.getRequest(), EXT_THEME);
		return StringUtils.isBlank(extTheme)?this.getSiteConfig().getExtTheme():extTheme;
	}
	
	@Override
	public boolean isJsui() {
		String js = ResponseUtils.getCookieValue(WebHolder.getRequest(), JSUI);
		if(StringUtils.isBlank(js)){
			return initIfNecessity().isJsui();
		}
		return "true".equalsIgnoreCase(js);
	}
	
	public void setJsui(boolean viewJsui){
		ResponseUtils.setCookie(WebHolder.getResponse(), JSUI, Boolean.toString(viewJsui));
		initIfNecessity().setJsui(viewJsui);
	}

	public void config(String theme, String layout, String view) {
		if(theme!=null)
			this.setThemeTag(theme);
		
		if(layout!=null)
			this.setLayout(layout);

		if(view!=null)
			this.setThemeView(view);
	}
	

	@Override
	public void config(Map<String, Object> config) {
		ReflectUtils.copyIgnoreBlank(config, this);
	}

	public void setThemeTag(String theme) {
		ResponseUtils.setCookie(WebHolder.getResponse(), THEME_TAG, theme);
		initIfNecessity().setThemeTag(theme);
	}

	public void setLayout(String layout) {
		ResponseUtils.setCookie(WebHolder.getResponse(), LAYOUT, layout);
		initIfNecessity().setLayout(layout);
	}

	public void setThemeView(String view) {
		ResponseUtils.setCookie(WebHolder.getResponse(), THEME_VIEW, view);
		initIfNecessity().setThemeView(view);
	}
	
	public void setExtTheme(String extTheme){
		ResponseUtils.setCookie(WebHolder.getResponse(), EXT_THEME, extTheme);
		initIfNecessity().setExtTheme(extTheme);
	}

}
