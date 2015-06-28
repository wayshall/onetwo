package org.onetwo.common.web.view;

import java.io.Serializable;
import java.util.Map;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.utils.WebUtils;
import org.onetwo.common.web.view.jsp.TagUtils;

public class DefaultTagThemeSetting implements ThemeSetting {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2444132630486770221L;
	public static String WEB_INF_DIR = "/WEB-INF";
	/****
	 * tomcat要求标签模板必须要放在/WEB-INF/tags下
	 */
	public static String BASE_TAG_DIR = "/WEB-INF/tags";
	public static String BASE_LAYOUT_DIR = "layout/";

//	private BaseSiteConfig siteConfig = BaseSiteConfig.getInstance();
	private ThemeAttributes attributes = new ThemeAttributes();
	
	public DefaultTagThemeSetting(){
		BaseSiteConfig siteConfig = BaseSiteConfig.getInstance();
		attributes.themeTag = siteConfig.getThemeTag();
		attributes.layout = siteConfig.getThemeLayoutDefaultPage();
		attributes.themeView = siteConfig.getThemeView();
		attributes.jsui = siteConfig.getThemeJsui();
	}
	
	
	@Override
	public String getViewPage(final String path) {
		if(path.startsWith(WebUtils.FORWARD_KEY)){
			String newpath = path.substring(WebUtils.FORWARD_KEY.length());
			newpath = getThemeView() + newpath;
			return WebUtils.forwardPrefix(newpath);
		}else if(path.startsWith(WebUtils.REDIRECT_KEY)){
			return path;
		}
		return getThemeView() + path;
	}

	@Override
	public String getLayoutPage(String path){
		String layoutPage = path;
		if(path!=null && path.startsWith("/")){
			layoutPage = TagUtils.getViewPage(path);
		}else{
			if(StringUtils.isBlank(layoutPage))
				layoutPage = this.getThemeLayoutDefaultPage();
			String baseLayoutDir = WEB_INF_DIR + getThemeView() + BASE_LAYOUT_DIR;
			layoutPage = StringUtils.appendEndWith(layoutPage, ".jsp");
			layoutPage = getDirPage(baseLayoutDir, layoutPage);
		}
		return layoutPage;
	}
	
	@Override
	public String getTagPage(String path){
		String t = StringUtils.appendEndWith(path, ".jsp");
		String baseTagDir = getThemeTag();
		baseTagDir = StringUtils.appendEndWith(baseTagDir, "/");
		if(StringUtils.isBlank(baseTagDir)){
			baseTagDir = BASE_TAG_DIR;
		}else if(baseTagDir.startsWith("/tags/")){
			baseTagDir = WEB_INF_DIR + baseTagDir;
		}else{
			baseTagDir = BASE_TAG_DIR + baseTagDir;
		}
		return getDirPage(baseTagDir, t);
	}
	
	
	@Override
	public void setJsui(boolean jsui) {
//		attributes.setJsui(jsui);
		//throw unsupportedexception
	}

	public String getThemeView() {
		return attributes.themeView;
	}

	public String getThemeTag() {
		return attributes.themeTag;
	}

	public String getThemeLayoutDefaultPage() {
		return attributes.layout;
	}

	protected String getDirPage(String baseDir, String path){
		if(StringUtils.isBlank(path))
			return path;
		
		baseDir = StringUtils.appendEndWith(baseDir, "/");
		if(path.startsWith(baseDir))
			return path;
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return baseDir + path;
	}

	@Override
	public String getExtTheme() {
		return attributes.extTheme;
	}

	protected BaseSiteConfig getSiteConfig() {
		return BaseSiteConfig.getInstance();
	}

	@Override
	public boolean isJsui() {
		return attributes.jsui;
	}
	
	@Override
	public void config(String theme, String layout, String view) {
//		throw new UnsupportedOperationException();
	}

	public void config(Map<String, Object> config) {
//		throw new UnsupportedOperationException();
	}


	protected static class ThemeAttributes implements Serializable {
		private String themeTag;
		private String layout;
		private String themeView;
		private String extTheme;
		private boolean jsui;
		
		public ThemeAttributes() {
			super();
		}
		public String getThemeTag() {
			return themeTag;
		}
		public void setThemeTag(String themeTag) {
			this.themeTag = themeTag;
		}
		public String getLayout() {
			return layout;
		}
		public void setLayout(String layout) {
			this.layout = layout;
		}
		public String getThemeView() {
			return themeView;
		}
		public void setThemeView(String themeView) {
			this.themeView = themeView;
		}
		public String getExtTheme() {
			return extTheme;
		}
		public void setExtTheme(String extTheme) {
			this.extTheme = extTheme;
		}
		public boolean isJsui() {
			return jsui;
		}
		public void setJsui(boolean jsui) {
			this.jsui = jsui;
		}
		
	}
	
}
